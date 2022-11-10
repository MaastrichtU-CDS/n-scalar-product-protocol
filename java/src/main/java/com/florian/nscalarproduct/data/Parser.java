package com.florian.nscalarproduct.data;

import com.florian.nscalarproduct.error.InvalidDataFormatException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public final class Parser {
    private static final String LOCALLY_PRESENT_COLUMN_NAME = "locallyPresent";

    private Parser() {
    }

    public static Data parseData(String path, int idColumn) throws IOException, InvalidDataFormatException {
        if (path.contains(".arff")) {
            return parseArff(path, idColumn);
        } else if (path.contains(".csv")) {
            return parseCsv(path, idColumn);
        } else if (path.contains(".parquet")) {
            return parseParquet(path, idColumn);
        } else {
            throw new InvalidDataFormatException();
        }
    }

    private static Data parseArff(String path, int idColumn) throws IOException {
        List<List<String>> records = readCsv(path);
        Map<String, Attribute> attributes = new HashMap<>();
        int locallyPresentColumn = -1;

        List<List<Attribute>> parsed = new ArrayList<>();

        int data = 0;
        for (int i = 0; i < records.size(); i++) {
            String start = records.get(i).get(0);
            if (start.contains("@attribute")) {
                //get name
                String name = start.replace("@attribute ", "");
                name = name.substring(0, name.indexOf(" "));

                //get type
                String type = "";
                if (start.contains("real")) {
                    type = "real";
                } else if (start.contains("numeric")) {
                    type = "numeric";
                } else if (start.toLowerCase().contains("true") || start.toLowerCase().contains("false")) {
                    type = "bool";
                } else {
                    type = "string";
                }
                Attribute a = new Attribute();
                a.setAttributeName(name);
                a.setType(Attribute.AttributeType.valueOf(type));
                attributes.put(name, a);
            } else if (start.contains("@data")) {
                //reached datablock
                data = i + 1;
                break;
            }
        }
        List<String> attributeOrder = records.get(data);
        data++;
        for (int i = 0; i < attributeOrder.size(); i++) {
            String name = attributeOrder.get(i);
            List<Attribute> attribute = new ArrayList<>();
            for (int j = data; j < records.size(); j++) {
                attribute.add(new Attribute(attributes.get(name).getType(), records.get(j).get(i),
                                            name));
            }
            parsed.add(attribute);
            if (attribute.get(0).getAttributeName().equals(LOCALLY_PRESENT_COLUMN_NAME)) {
                locallyPresentColumn = i;
            }
        }

        return new Data(idColumn, locallyPresentColumn, parsed);
    }

    private static Data parseParquet(String path, int idColumn) throws IOException {
        List<SimpleGroup> simpleGroups = new ArrayList<>();
        ParquetFileReader reader = ParquetFileReader.open(
                HadoopInputFile.fromPath(new Path(path), new Configuration(true)));
        MessageType schema = reader.getFooter().getFileMetaData().getSchema();
        PageReadStore pages;
        while ((pages = reader.readNextRowGroup()) != null) {
            long rows = pages.getRowCount();
            MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
            RecordReader recordReader = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));

            for (int i = 0; i < rows; i++) {
                SimpleGroup simpleGroup = (SimpleGroup) recordReader.read();
                simpleGroups.add(simpleGroup);
            }
        }
        reader.close();

        List<Type> types = schema.getFields();
        List<List<Attribute>> parsed = new ArrayList<>();
        int locallyPresentColumn = -1;
        for (int i = 0; i < types.size(); i++) {
            List<Attribute> attribute = new ArrayList<>();
            String type = transformtype(types.get(i));
            for (int j = 0; j < simpleGroups.size(); j++) {
                SimpleGroup ind = simpleGroups.get(j);
                attribute.add(
                        new Attribute(Attribute.AttributeType.valueOf(type), simpleGroups.get(j).getValueToString(i, 0),
                                      types.get(i).getName()));
            }
            parsed.add(attribute);
            if (attribute.get(0).getAttributeName().equals(LOCALLY_PRESENT_COLUMN_NAME)) {
                locallyPresentColumn = i;
            }
        }

        return new Data(idColumn, locallyPresentColumn, parsed);
    }

    private static String transformtype(Type type) {
        String t = ((PrimitiveType) type).getPrimitiveTypeName().toString();
        if (t.contains("INT")) {
            return "numeric";
        } else if (t.contains("DOUBLE")) {
            return "real";
        } else if (t.contains("BINARY")) {
            return "string";
        } else if (t.contains("BOOLEAN")) {
            return "bool";
        }
        return "";
    }

    private static Data parseCsv(String path, int idColumn) {
        List<List<String>> records = readCsv(path);
        List<String> types = records.get(0);
        List<String> attributes = records.get(1);
        List<List<Attribute>> parsed = new ArrayList<>();
        int locallyPresentColumn = -1;

        for (int i = 0; i < attributes.size(); i++) {
            List<Attribute> attribute = new ArrayList<>();
            for (int j = 2; j < records.size(); j++) {
                attribute.add(new Attribute(Attribute.AttributeType.valueOf(types.get(i)), records.get(j).get(i),
                                            attributes.get(i)));
            }
            parsed.add(attribute);
            if (attribute.get(0).getAttributeName().equals(LOCALLY_PRESENT_COLUMN_NAME)) {
                locallyPresentColumn = i;
            }
        }

        Data data = new Data(idColumn, locallyPresentColumn, parsed);
        return data;
    }

    private static List<List<String>> readCsv(String path) {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
