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
-- Name: permission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE permission (
    id bigint DEFAULT nextval('permission_id_seq'::regclass) NOT NULL,
    createddate date DEFAULT ('now'::text)::date NOT NULL,
    isactive integer DEFAULT 1,
    updated timestamp without time zone,
    updatedby character varying(255),
    description character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE permission OWNER TO postgres;

--
-- Name: permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- Name: uk_2ojme20jpga3r4r79tdso17gi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT uk_2ojme20jpga3r4r79tdso17gi UNIQUE (name);


--
-- Name: idx_permission_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_permission_name ON permission USING btree (name);


--
-- PostgreSQL database dump complete
--

