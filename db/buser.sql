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
-- Name: buser; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE buser (
    id bigint NOT NULL,
    created timestamp without time zone,
    createdby character varying(255),
    isactive integer DEFAULT 1,
    refid character varying(45) NOT NULL,
    updated timestamp without time zone,
    updatedby character varying(255),
    email character varying(100),
    firstname character varying(255) NOT NULL,
    isarchived boolean NOT NULL,
    lastname character varying(255) NOT NULL,
    password character varying(255),
    userid character varying(255) NOT NULL,
    orgid bigint,
    pictureurl character varying(255),
    refreshtoken character varying(255)
);


ALTER TABLE buser OWNER TO postgres;

--
-- Name: buser_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE buser_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE buser_id_seq OWNER TO postgres;

--
-- Name: buser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE buser_id_seq OWNED BY buser.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser ALTER COLUMN id SET DEFAULT nextval('buser_id_seq'::regclass);


--
-- Name: buser_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_pkey PRIMARY KEY (id);


--
-- Name: buser_refid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_refid_key UNIQUE (refid);


--
-- Name: buser_userid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_userid_key UNIQUE (userid);


--
-- Name: uk_re2sfgtejo19tdfb4sq3mqwwq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT uk_re2sfgtejo19tdfb4sq3mqwwq UNIQUE (userid);


--
-- Name: fk3ca6f2df808a684; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT fk3ca6f2df808a684 FOREIGN KEY (orgid) REFERENCES orgmodel(id);


--
-- PostgreSQL database dump complete
--

