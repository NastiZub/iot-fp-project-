--
-- PostgreSQL database dump
--

\restrict 7V8rYPn1fOVsVB5nRYMUuQsvYsLAkRIaYHycIjEt0zgM4NdwEt9RV5qp1anKXjX

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
-- Data for Name: devices; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.devices (id, name, device_type, created_at) FROM stdin;
1	ESP32 Main	ESP32	2026-03-25 15:09:17.337642
2	Raspberry Pi Main	Raspberry Pi	2026-03-25 15:09:17.337642
\.


--
-- Name: devices_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.devices_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

\unrestrict 7V8rYPn1fOVsVB5nRYMUuQsvYsLAkRIaYHycIjEt0zgM4NdwEt9RV5qp1anKXjX

