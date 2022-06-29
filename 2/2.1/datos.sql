INSERT INTO Ciudad(cod_postal, nombre)VALUES (1,"ciudad_c1");
INSERT INTO Ciudad(cod_postal, nombre)VALUES (2,"ciudad_c2");

INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal)VALUES ("sucursal_n1", "sucursal_dir1", "sucursal_te1", "sucursal_h1",1);
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal)VALUES ("sucursal_n2", "sucursal_dir2", "sucursal_te2", "sucursal_h2",2);

INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc)VALUES ("empleado_ap1","empleado_n1","empleado_t1",1,"empleado_dir1","empleado_te1", "empleado_c1",md5("empleado_pass1"),1);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc)VALUES ("empleado_ap2","empleado_n2","empleado_t2",2,"empleado_dir2","empleado_te2", "empleado_c2",md5("empleado_pass2"),2);

INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac)VALUES ("cliente_ap1", "cliente_n1", "cliente_t1", 1, "cliente_dir1", "cliente_te1", "1/1/1");
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac)VALUES ("cliente_ap2", "cliente_n2", "cliente_t2", 2, "cliente_dir2", "cliente_te2", "2/2/2");
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac)VALUES ("cliente_ap3", "cliente_n3", "cliente_t3", 3, "cliente_dir3", "cliente_te3", "3/3/3");

#Datos relacionados con el Plazo Fijo

INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc)VALUES (1000,"1/1/1","2/2/2",5,20,1);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc)VALUES (2000,"2/2/2","3/3/3",6,18,2);
INSERT INTO Tasa_Plazo_Fijo(periodo, monto_inf, monto_sup, tasa)VALUES (120,500,3000,5);
INSERT INTO Tasa_Plazo_Fijo(periodo, monto_inf, monto_sup, tasa)VALUES (80,200,2500,6);
INSERT INTO Plazo_Cliente(nro_plazo, nro_cliente)VALUES (1,1);
INSERT INTO Plazo_Cliente(nro_plazo, nro_cliente)VALUES (2,2);

#Datos relacionados con el Préstamo

INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente)VALUES ("1/1/1",12,10000,7,10,833,1,1);
INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente)VALUES ("2/2/2",24,20000,8,11,833,2,2);

INSERT INTO Pago(nro_prestamo, nro_pago, fecha_venc, fecha_pago)VALUES (1,1,"2/2/2","1/1/1");
INSERT INTO Pago(nro_prestamo, nro_pago, fecha_venc, fecha_pago)VALUES (2,2,"3/3/3","2/2/2");

INSERT INTO Tasa_Prestamo(periodo, monto_inf, monto_sup, tasa)VALUES (180,5000,30000,5);
INSERT INTO Tasa_Prestamo(periodo, monto_inf, monto_sup, tasa)VALUES (240,7000,32000,8);

# Datos relacionados con la caja de ahorro

INSERT INTO Caja_Ahorro (CBU, saldo)VALUES (111,10000);
INSERT INTO Caja_Ahorro (CBU, saldo)VALUES (222,20000);
INSERT INTO Caja_Ahorro (CBU, saldo)VALUES (333,3000);
INSERT INTO Cliente_CA (nro_cliente, nro_ca)VALUES (1,1);
INSERT INTO Cliente_CA (nro_cliente, nro_ca)VALUES (2,2);
INSERT INTO Cliente_CA (nro_cliente, nro_ca)VALUES (2,3);
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca)VALUES (md5("111"),md5("100"),"1/1/1", 1, 1);
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca)VALUES (md5("222"),md5("200"),"2/2/2", 2, 2);
INSERT INTO Caja VALUES();
INSERT INTO Caja VALUES();
INSERT INTO Ventanilla (cod_caja,nro_suc)VALUES (1,1);
INSERT INTO Ventanilla (cod_caja,nro_suc)VALUES (2,2);
INSERT INTO ATM (cod_caja, cod_postal, direccion)VALUES (1,1,"atm_dir1");
INSERT INTO ATM (cod_caja, cod_postal, direccion)VALUES (2,2,"atm_dir2");

#depósito
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("1/1/1","01:01:01",1000);
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("2/2/2","02:02:02",2000);

#transferencia
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("3/3/3","03:03:03",3000);
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("4/4/4", "04:04:04",4000);

#extracción
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("5/5/5","05:05:05",5000);
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("6/6/6","06:06:06",6000);

#débito
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("7/7/7","07:07:07",7000);
INSERT INTO Transaccion (fecha,hora,monto) VALUES ("8/8/8","08:08:08",8000);

INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (1,1);
INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (2,2);
INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (3,1);
INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (4,2);
INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (5,1);
INSERT INTO Transaccion_por_caja (nro_trans,cod_caja)VALUES (6,2);

INSERT INTO Deposito (nro_trans, nro_ca)VALUES (1,1);
INSERT INTO Deposito (nro_trans, nro_ca)VALUES (2,2);
INSERT INTO Transferencia (nro_trans, nro_cliente, origen, destino)VALUES (3,1,1,2);
INSERT INTO Transferencia (nro_trans, nro_cliente, origen, destino)VALUES (4,2,2,3);
INSERT INTO Extraccion (nro_trans, nro_cliente, nro_ca) VALUES(5,1,1);
INSERT INTO Extraccion (nro_trans, nro_cliente, nro_ca)VALUES (6,2,2);
INSERT INTO Debito (nro_trans,descripcion, nro_cliente, nro_ca)VALUES (7,"debito_d1", 1, 1);
INSERT INTO Debito (nro_trans,descripcion, nro_cliente, nro_ca)VALUES (8,"debito_d2", 2, 2);
