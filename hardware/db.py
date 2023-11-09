import psycopg2

# Establishing the connection
conn = psycopg2.connect(
    dbname="project",
    user="postgres",
    password="Sairen1360!",
    host="145.126.3.135",
    port="5432"
)

# Creating a cursor object using the cursor() method
cursor = conn.cursor()

# Executing a PostgreSQL query
cursor.execute("SELECT version();")

# Fetching the results
record = cursor.fetchone()
print("You are connected to - ", record, "\n")

# Closing the connection
conn.close()
