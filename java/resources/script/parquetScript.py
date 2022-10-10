import pandas
import pyarrow

data = pandas.read_csv("horizontalsplit.csv", header=None)

headers = data.iloc[0]

no_headers = pandas.read_csv("horizontalsplit.csv", skiprows=1)
for i,col in enumerate(no_headers.columns):
    if headers[i] == "string":
        no_headers[col] = no_headers[col].astype('str')
    elif headers[i] == "bool":
        no_headers[col] = no_headers[col].astype('bool')
    elif headers[i] == "numeric":
        no_headers[col] = no_headers[col].astype('int')
    elif headers[i] == "real":
        no_headers[col] = no_headers[col].astype('float')


parquet_file = 'tabelletje.parquet'
no_headers.to_parquet(parquet_file)

parquet = pandas.read_parquet(parquet_file)
print(parquet)
