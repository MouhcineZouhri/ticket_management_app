FROM container-registry.oracle.com/database/free:latest

ENV ORACLE_HOME=/opt/oracle/product/19c/dbhome_1
ENV ORACLE_SID=ORCL
ENV ORACLE_PDB=PDB1
ENV ORACLE_PASSWORD=your_password_here

EXPOSE 1521 5500

COPY init.sql /opt/oracle/scripts/setup/init.sql

COPY start.sh /start.sh
RUN chmod +x /start.sh

CMD ["/start.sh"]
