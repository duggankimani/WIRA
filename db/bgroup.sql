--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: bgroup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE bgroup (
    id bigint NOT NULL,
    created timestamp without time zone,
    createdby character varying(255),
    isactive integer DEFAULT 1,
    refid character varying(45) NOT NULL,
    updated timestamp without time zone,
    updatedby character varying(255),
    fullname character varying(255),
    isarchived boolean NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE bgroup OWNER TO postgres;

--
-- Name: bgroup_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bgroup_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bgroup_id_seq OWNER TO postgres;

--
-- Name: bgroup_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bgroup_id_seq OWNED BY bgroup.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup ALTER COLUMN id SET DEFAULT nextval('bgroup_id_seq'::regclass);


--
-- Name: bgroup_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_name_key UNIQUE (name);


--
-- Name: bgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_pkey PRIMARY KEY (id);


--
-- Name: bgroup_refid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_refid_key UNIQUE (refid);


--
-- Name: uk_5spukcbu0ojd3lku4asyarbq9; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT uk_5spukcbu0ojd3lku4asyarbq9 UNIQUE (name);


--
-- PostgreSQL database dump complete
--

