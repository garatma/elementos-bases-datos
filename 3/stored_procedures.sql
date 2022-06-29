delimiter !
create procedure extr (in monto decimal(16,2), in cod_atm int, in cli int, in ca int)
begin	
	declare nuevo_saldo decimal(16,2) default 0;
	declare hora_actual time;
	declare fecha_actual date;
	declare exit handler for sqlexception
	begin
		select 'Error en el cómputo de la transferencia. Transacción abortada.' as resultado;
		rollback;
	end;
	select curtime() into hora_actual;
	select curdate() into fecha_actual;
	start transaction;
	if monto > 0 then 
		if exists ( select * from atm where cod_caja = cod_atm ) then
			if exists ( select * from caja_ahorro where nro_ca = ca ) then		
				if exists ( select * from cliente where nro_cliente = cli ) then
					if exists ( select * from cliente_ca where nro_cliente = cli and nro_ca = ca ) then 
						if exists ( select * from caja_ahorro where saldo >= monto and nro_ca = ca ) then
						
							# se crea una nueva transacción
							insert into transaccion (fecha, hora, monto) values (fecha_actual,hora_actual,monto);
							
							# se crea una nueva transaccion_por_caja
							insert into transaccion_por_caja (cod_caja, nro_trans) values (cod_atm,last_insert_id());
							
							# se crea una nueva extracción
							insert into extraccion (nro_trans, nro_cliente, nro_ca) values (last_insert_id(), cli, ca);
							
							# actualización del saldo de la caja de ahorro
							update caja_ahorro set saldo = saldo - monto where nro_ca = ca;
						
							select "La extracción se realizó con éxito." as resultado;
						
						else select "Error: no se puede extraer un monto mayor al saldo disponible en la caja de ahorro. Transacción abortada." as resultado;
						end if;
					else select "Error: el cliente no es el titular de la caja. Transacción abortada." as resultado;
					end if;
				else select "Error: no existe tal cliente. Transacción abortada." as resultado;
				end if;
			else select "Error: no existe tal caja de ahorro. Transacción abortada." as resultado;
			end if;
		else select "Error: no existe tal ATM. Transacción abortada" as resultado;
		end if;
	else select "Error: el monto a extraer no puede ser cero o negativo. Transacción abortada." as resultado;
	end if;
	commit;
end; !
delimiter ;

delimiter !
create procedure transf (in monto decimal(16,2), in cod_atm int, in cli int, in ca_origen int, in ca_destino int)
begin	
	declare nuevo_saldo decimal(16,2) default 0;
	declare hora_actual time;
	declare fecha_actual date;
	declare exit handler for sqlexception
	begin
		select 'Error en el cómputo de la transferencia. Transacción abortada.' as resultado;
		rollback;
	end;
	select curtime() into hora_actual;
	select curdate() into fecha_actual;
	start transaction;
	if ca_origen <> ca_destino then 
		if monto > 0 then 
			if exists ( select * from atm where cod_caja = cod_atm ) then
				if exists ( select * from caja_ahorro where nro_ca = ca_origen ) then		
					if exists ( select * from cliente where nro_cliente = cli ) then
						if exists ( select * from cliente_ca where nro_cliente = cli and nro_ca = ca_origen ) then 
							if exists ( select * from caja_ahorro where saldo >= monto and nro_ca = ca_origen ) then
								if exists ( select * from caja_ahorro where nro_ca = ca_destino ) then
								
									# creación de la extracción de la caja origen
										# se crea una nueva transacción
										insert into transaccion (fecha, hora, monto) values (fecha_actual,hora_actual,monto);
										
										# se crea una nueva transaccion_por_caja
										insert into transaccion_por_caja (cod_caja, nro_trans) values (cod_atm,last_insert_id());
										
										# se crea una nueva extracción
										insert into extraccion (nro_trans, nro_cliente, nro_ca) values (last_insert_id(), cli, ca_origen);
									
									#creación del depósito de la caja destino
										# se crea una nueva transacción
										insert into transaccion (fecha, hora, monto) values (fecha_actual,hora_actual,monto);
										
										# se crea una nueva transaccion_por_caja
										insert into transaccion_por_caja (cod_caja, nro_trans) values (cod_atm,last_insert_id());
										
										# se crea un nuevo depósito
										insert into deposito (nro_trans, nro_ca) values (last_insert_id(), ca_destino);
									
									# creación de la transferencia
										# se crea una nueva transacción
										insert into transaccion (fecha, hora, monto) values (fecha_actual,hora_actual,monto);
										
										# se crea una nueva transaccion_por_caja
										insert into transaccion_por_caja (cod_caja, nro_trans) values (cod_atm,last_insert_id());
										
										# se crea una nueva transferencia
										insert into transferencia (nro_trans, nro_cliente, origen, destino) values (last_insert_id(), cli, ca_origen, ca_destino);
									
									# actualización del saldo de las cajas de ahorro origen y destino
									update caja_ahorro set saldo = saldo - monto where nro_ca = ca_origen;
									update caja_ahorro set saldo = saldo + monto where nro_ca = ca_destino;
									
									select "La transferencia se realizó con éxito." as resultado;
									
								else select "Error: no existe tal caja de ahorro destino. Transacción abortada." as resultado;
								end if;
							else select "Error: no se puede transferir un monto mayor al saldo disponible en la caja de ahorro origen. Transacción abortada." as resultado;
							end if;
						else select "Error: el cliente no es el titular de la caja de ahorro origen. Transacción abortada." as resultado;
						end if;
					else select "Error: no existe tal cliente. Transacción abortada." as resultado;
					end if;
				else select "Error: no existe tal caja de ahorro de origen. Transacción abortada." as resultado;
				end if;
			else select "Error: no existe tal ATM. Transacción abortada" as resultado;
			end if;
		else select "Error: el monto a extraer no puede ser cero o negativo. Transacción abortada." as resultado;
		end if;
	else select "Error: las cajas de ahorro deben ser distintas para realizar la transferencia. Transacción abortada." as resultado;
	end if;
	commit;
end; !
delimiter ;
