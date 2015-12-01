@echo off
echo Restoring database radio_audit ...
"/usr/bin/mysql" --line-numbers -h localhost -u radioAudit -pcielo01 radio_audit < create-db-desa.sql
echo Database radio_audit restored
