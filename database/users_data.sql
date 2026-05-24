--
-- PostgreSQL database dump
--

\restrict HPd0o5EVefWdn3IKkzCICwce8K20Z3qxBH0eewWZhRfIVrbDnNT21pScFdLCmUr

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
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, email, password_hash, role, created_at) FROM stdin;
4	ADMIN	admin@admin.com	$2a$10$ICD8Cpglt2zeaUWCkkxiAO2uEic8Yr0B1g3yZK5JERQFde7ohYgKG	ADMIN	2026-05-24 07:35:20.627165
5	test	test@test.com	$2a$10$BGXmWbsUkIzWPNgXbhzUGuVaMRF/CpQurPpCskrBFliLXbmz2BSUW	USER	2026-05-24 07:40:44.973817
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 5, true);


--
-- PostgreSQL database dump complete
--

\unrestrict HPd0o5EVefWdn3IKkzCICwce8K20Z3qxBH0eewWZhRfIVrbDnNT21pScFdLCmUr

