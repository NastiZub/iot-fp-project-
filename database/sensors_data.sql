--
-- PostgreSQL database dump
--

\restrict Mdp6qYz81lEgGc8rwCwVJNLhEKhYxWVpKRdJUNJWg9WDKYgg4H96FD9JOkuU2MB

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: sensors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sensors (id, device_id, sensor_type_id, name, is_active, connection_info, created_at) FROM stdin;
1	1	1	BME280 Temp	t	I2C	2026-03-25 15:19:08.722358
2	1	2	BME280 Humidity	t	I2C	2026-03-25 15:19:08.722358
3	1	3	BME280 Pressure	t	I2C	2026-03-25 15:19:08.722358
4	1	1	DS18B20 Temp	t	OneWire	2026-03-25 15:19:16.152568
5	1	4	BH1750 Light	t	I2C	2026-03-25 15:23:34.081004
6	1	5	VL53L1X Distance	t	I2C	2026-03-25 15:23:34.081004
7	1	6	HC-SR501 PIR	t	GPIO	2026-03-25 15:23:34.081004
8	1	7	Touch Sensor	t	GPIO	2026-03-25 15:26:23.048807
9	2	8	RFID MFRC522	t	SPI	2026-03-25 15:26:23.048807
10	1	10	Obstacle Sensor	t	GPIO	2026-03-25 15:26:23.048807
11	1	9	Speed Sensor	t	GPIO	2026-03-25 15:26:23.048807
12	1	14	INA219 Voltage	t	I2C	2026-03-25 15:37:43.1574
13	1	13	INA219 Current	t	I2C	2026-03-25 15:37:43.1574
14	1	15	INA219 Power	t	I2C	2026-03-25 15:37:43.1574
15	1	5	HC-SR04 Ultrasonic	t	GPIO	2026-03-25 15:37:43.1574
16	1	6	Reed Switch	t	GPIO	2026-03-25 15:37:43.1574
21	1	11	BMI160 Acceleration	t	I2C	2026-03-25 15:37:43.1574
22	1	12	BMI160 Gyroscope	t	I2C	2026-03-25 15:37:43.1574
23	1	11	MPU6050 Acceleration	t	I2C	2026-03-25 15:37:43.1574
24	1	12	MPU6050 Gyroscope	t	I2C	2026-03-25 15:37:43.1574
25	1	16	MQ135 Gas Sensor	t	ADC	2026-03-25 15:45:05.69979
\.


--
-- Name: sensors_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sensors_id_seq', 26, true);


--
-- PostgreSQL database dump complete
--

\unrestrict Mdp6qYz81lEgGc8rwCwVJNLhEKhYxWVpKRdJUNJWg9WDKYgg4H96FD9JOkuU2MB

