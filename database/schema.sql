--
-- PostgreSQL database dump
--

\restrict NbDawvafEeCW5PFB8lcBzMX5kIvRKgj2c6h2PTpAurdCfEyeTrhPaP7eiOX3bpl

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: actuators; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.actuators (
    id integer NOT NULL,
    device_id integer NOT NULL,
    name character varying(100) NOT NULL,
    actuator_type character varying(50) NOT NULL,
    state character varying(20),
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.actuators OWNER TO postgres;

--
-- Name: actuators_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.actuators_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.actuators_id_seq OWNER TO postgres;

--
-- Name: actuators_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.actuators_id_seq OWNED BY public.actuators.id;


--
-- Name: automation_rules; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.automation_rules (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    source_sensor_id integer NOT NULL,
    condition_operator character varying(20) NOT NULL,
    condition_value character varying(50) NOT NULL,
    action_type character varying(50) NOT NULL,
    target_sensor_id integer,
    is_enabled boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.automation_rules OWNER TO postgres;

--
-- Name: automation_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.automation_rules_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.automation_rules_id_seq OWNER TO postgres;

--
-- Name: automation_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.automation_rules_id_seq OWNED BY public.automation_rules.id;


--
-- Name: camera_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.camera_events (
    id integer NOT NULL,
    device_id integer NOT NULL,
    detected_object character varying(100) NOT NULL,
    confidence double precision,
    image_path character varying(255),
    detected_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.camera_events OWNER TO postgres;

--
-- Name: camera_events_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.camera_events_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.camera_events_id_seq OWNER TO postgres;

--
-- Name: camera_events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.camera_events_id_seq OWNED BY public.camera_events.id;


--
-- Name: devices; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.devices (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    device_type character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.devices OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.devices_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.devices_id_seq OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.devices_id_seq OWNED BY public.devices.id;


--
-- Name: measurement_sessions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.measurement_sessions (
    id integer NOT NULL,
    user_id integer NOT NULL,
    mode character varying(30) NOT NULL,
    scope character varying(30) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.measurement_sessions OWNER TO postgres;

--
-- Name: measurement_sessions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.measurement_sessions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.measurement_sessions_id_seq OWNER TO postgres;

--
-- Name: measurement_sessions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.measurement_sessions_id_seq OWNED BY public.measurement_sessions.id;


--
-- Name: measurements; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.measurements (
    id integer NOT NULL,
    sensor_id integer NOT NULL,
    value double precision NOT NULL,
    measured_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id integer,
    session_id integer
);


ALTER TABLE public.measurements OWNER TO postgres;

--
-- Name: measurements_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.measurements_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.measurements_id_seq OWNER TO postgres;

--
-- Name: measurements_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.measurements_id_seq OWNED BY public.measurements.id;


--
-- Name: rfid_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rfid_tags (
    id integer NOT NULL,
    tag_uid character varying(100) NOT NULL,
    user_id integer NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    tag_name character varying(100)
);


ALTER TABLE public.rfid_tags OWNER TO postgres;

--
-- Name: rfid_tags_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rfid_tags_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rfid_tags_id_seq OWNER TO postgres;

--
-- Name: rfid_tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rfid_tags_id_seq OWNED BY public.rfid_tags.id;


--
-- Name: sensor_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensor_types (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    unit character varying(20)
);


ALTER TABLE public.sensor_types OWNER TO postgres;

--
-- Name: sensor_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sensor_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sensor_types_id_seq OWNER TO postgres;

--
-- Name: sensor_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sensor_types_id_seq OWNED BY public.sensor_types.id;


--
-- Name: sensors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensors (
    id integer NOT NULL,
    device_id integer NOT NULL,
    sensor_type_id integer NOT NULL,
    name character varying(100) NOT NULL,
    is_active boolean DEFAULT true,
    connection_info character varying(100),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.sensors OWNER TO postgres;

--
-- Name: sensors_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sensors_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sensors_id_seq OWNER TO postgres;

--
-- Name: sensors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sensors_id_seq OWNED BY public.sensors.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(100) NOT NULL,
    email character varying(150),
    password_hash character varying(255) NOT NULL,
    role character varying(50) DEFAULT 'user'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: actuators id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.actuators ALTER COLUMN id SET DEFAULT nextval('public.actuators_id_seq'::regclass);


--
-- Name: automation_rules id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automation_rules ALTER COLUMN id SET DEFAULT nextval('public.automation_rules_id_seq'::regclass);


--
-- Name: camera_events id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.camera_events ALTER COLUMN id SET DEFAULT nextval('public.camera_events_id_seq'::regclass);


--
-- Name: devices id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devices ALTER COLUMN id SET DEFAULT nextval('public.devices_id_seq'::regclass);


--
-- Name: measurement_sessions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurement_sessions ALTER COLUMN id SET DEFAULT nextval('public.measurement_sessions_id_seq'::regclass);


--
-- Name: measurements id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurements ALTER COLUMN id SET DEFAULT nextval('public.measurements_id_seq'::regclass);


--
-- Name: rfid_tags id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rfid_tags ALTER COLUMN id SET DEFAULT nextval('public.rfid_tags_id_seq'::regclass);


--
-- Name: sensor_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensor_types ALTER COLUMN id SET DEFAULT nextval('public.sensor_types_id_seq'::regclass);


--
-- Name: sensors id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors ALTER COLUMN id SET DEFAULT nextval('public.sensors_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: actuators actuators_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.actuators
    ADD CONSTRAINT actuators_pkey PRIMARY KEY (id);


--
-- Name: automation_rules automation_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automation_rules
    ADD CONSTRAINT automation_rules_pkey PRIMARY KEY (id);


--
-- Name: camera_events camera_events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.camera_events
    ADD CONSTRAINT camera_events_pkey PRIMARY KEY (id);


--
-- Name: devices devices_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devices
    ADD CONSTRAINT devices_pkey PRIMARY KEY (id);


--
-- Name: measurement_sessions measurement_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurement_sessions
    ADD CONSTRAINT measurement_sessions_pkey PRIMARY KEY (id);


--
-- Name: measurements measurements_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurements
    ADD CONSTRAINT measurements_pkey PRIMARY KEY (id);


--
-- Name: rfid_tags rfid_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rfid_tags
    ADD CONSTRAINT rfid_tags_pkey PRIMARY KEY (id);


--
-- Name: rfid_tags rfid_tags_tag_uid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rfid_tags
    ADD CONSTRAINT rfid_tags_tag_uid_key UNIQUE (tag_uid);


--
-- Name: sensor_types sensor_types_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensor_types
    ADD CONSTRAINT sensor_types_code_key UNIQUE (code);


--
-- Name: sensor_types sensor_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensor_types
    ADD CONSTRAINT sensor_types_pkey PRIMARY KEY (id);


--
-- Name: sensors sensors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT sensors_pkey PRIMARY KEY (id);


--
-- Name: rfid_tags unique_rfid_tag_uid; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rfid_tags
    ADD CONSTRAINT unique_rfid_tag_uid UNIQUE (tag_uid);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: actuators fk_actuators_device; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.actuators
    ADD CONSTRAINT fk_actuators_device FOREIGN KEY (device_id) REFERENCES public.devices(id);


--
-- Name: camera_events fk_camera_device; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.camera_events
    ADD CONSTRAINT fk_camera_device FOREIGN KEY (device_id) REFERENCES public.devices(id);


--
-- Name: measurement_sessions fk_measurement_sessions_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurement_sessions
    ADD CONSTRAINT fk_measurement_sessions_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: measurements fk_measurements_sensor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurements
    ADD CONSTRAINT fk_measurements_sensor FOREIGN KEY (sensor_id) REFERENCES public.sensors(id);


--
-- Name: measurements fk_measurements_session; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurements
    ADD CONSTRAINT fk_measurements_session FOREIGN KEY (session_id) REFERENCES public.measurement_sessions(id);


--
-- Name: measurements fk_measurements_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.measurements
    ADD CONSTRAINT fk_measurements_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: rfid_tags fk_rfid_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rfid_tags
    ADD CONSTRAINT fk_rfid_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: automation_rules fk_rules_source_sensor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automation_rules
    ADD CONSTRAINT fk_rules_source_sensor FOREIGN KEY (source_sensor_id) REFERENCES public.sensors(id);


--
-- Name: automation_rules fk_rules_target_sensor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automation_rules
    ADD CONSTRAINT fk_rules_target_sensor FOREIGN KEY (target_sensor_id) REFERENCES public.sensors(id);


--
-- Name: sensors fk_sensors_device; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT fk_sensors_device FOREIGN KEY (device_id) REFERENCES public.devices(id);


--
-- Name: sensors fk_sensors_sensor_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT fk_sensors_sensor_type FOREIGN KEY (sensor_type_id) REFERENCES public.sensor_types(id);


--
-- PostgreSQL database dump complete
--

\unrestrict NbDawvafEeCW5PFB8lcBzMX5kIvRKgj2c6h2PTpAurdCfEyeTrhPaP7eiOX3bpl

