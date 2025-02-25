#!/bin/bash

# Start Oracle Database
echo "Starting Oracle Database..."
/etc/init.d/oracledb_ORCL configure

# Wait until the database is ready to accept connections
echo "Waiting for Oracle Database to be ready..."
until sqlplus -S /nolog <<EOF
CONNECT sys/$ORACLE_PASSWORD@//localhost:1521/$ORACLE_SID AS SYSDBA
EXIT;
EOF
do
    echo "Waiting for Oracle to start..."
    sleep 10
done

# Once the database is up, run the initialization SQL script
echo "Running initialization SQL script..."
sqlplus sys/$ORACLE_PASSWORD@//localhost:1521/$ORACLE_SID AS SYSDBA <<EOF
@/opt/oracle/scripts/setup/init.sql
EXIT;
EOF

echo "Oracle Database is ready!"
tail -f /dev/null  # Keeps the container running
