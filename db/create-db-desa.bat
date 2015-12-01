@echo off
echo Restoring database radio_audit ...
"C:\Archivos de programa\MySQL\MySQL Server 5.5\bin\mysql.exe" --line-numbers -h localhost -u radioAudit -pcielo01 radio_audit < create-db-desa.sql
echo Database radio_audit restored
