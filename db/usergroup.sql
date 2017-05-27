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
-- Name: usergroup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usergroup (
    userid bigint NOT NULL,
    groupid bigint NOT NULL
);


ALTER TABLE usergroup OWNER TO postgres;

--
-- Name: fk8a5be154220bf979; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usergroup
    ADD CONSTRAINT fk8a5be154220bf979 FOREIGN KEY (groupid) REFERENCES bgroup(id);


--
-- Name: fk8a5be15473e396d1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usergroup
    ADD CONSTRAINT fk8a5be15473e396d1 FOREIGN KEY (userid) REFERENCES buser(id);


--
-- PostgreSQL database dump complete
--

