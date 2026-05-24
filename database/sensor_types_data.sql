--
-- PostgreSQL database dump
--

\restrict 4BlpLkMbz67rFBh1Y3eOfBcr2HlDjh6UYTk5E1CtrsGDuvNjmncJGdJOhDiq8qW

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
-- Data for Name: sensor_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sensor_types (id, code, name, unit) FROM stdin;
1	temperature	Temperature Sensor	°C
2	humidity	Humidity Sensor	%
3	pressure	Pressure Sensor	hPa
4	light	Light Sensor	lux
5	distance	Distance Sensor	cm
6	motion	Motion Sensor	boolean
7	touch	Touch Sensor	boolean
8	rfid	RFID Reader	tag
10	obstacle	Obstacle Sensor	boolean
11	acceleration	Acceleration Sensor	m/s²
12	gyroscope	Gyroscope Sensor	deg/s
13	current	Current Sensor	A
14	voltage	Voltage Sensor	V
15	power	Power Sensor	W
9	speed	Speed Sensor	m/s
16	gas	Gas Sensor	ppm
17	servo	Servo Motor	degrees
\.


--
-- Name: sensor_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sensor_types_id_seq', 17, true);


--
-- PostgreSQL database dump complete
--

\unrestrict 4BlpLkMbz67rFBh1Y3eOfBcr2HlDjh6UYTk5E1CtrsGDuvNjmncJGdJOhDiq8qW

