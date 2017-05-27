--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE role_permissions (
    roleid bigint NOT NULL,
    permissionid bigint NOT NULL
);


ALTER TABLE role_permissions OWNER TO postgres;

--
-- Name: role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (roleid, permissionid);


--
-- Name: fkead9d23b5df964e4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT fkead9d23b5df964e4 FOREIGN KEY (permissionid) REFERENCES permission(id);


--
-- Name: fkead9d23bd965e1b0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT fkead9d23bd965e1b0 FOREIGN KEY (roleid) REFERENCES bgroup(id);


--
-- PostgreSQL database dump complete
--

