CREATE TABLE adfieldjson (
    id bigint NOT NULL,
    created timestamp without time zone,
    createdby character varying(255),
    isactive integer DEFAULT 1,
    refid character varying(45) NOT NULL,
    updated timestamp without time zone,
    updatedby character varying(255),
    field jsonb,
    formref character varying(255),
    fieldtype character varying(255)
);


ALTER TABLE adfieldjson OWNER TO postgres;

--
-- Name: adfieldjson_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE adfieldjson_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE adfieldjson_id_seq OWNER TO postgres;

--
-- Name: adfieldjson_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE adfieldjson_id_seq OWNED BY adfieldjson.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY adfieldjson ALTER COLUMN id SET DEFAULT nextval('adfieldjson_id_seq'::regclass);


--
-- Name: adfieldjson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY adfieldjson
    ADD CONSTRAINT adfieldjson_pkey PRIMARY KEY (id);


--
-- Name: adfieldjson_refid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY adfieldjson
    ADD CONSTRAINT adfieldjson_refid_key UNIQUE (refid);


--
-- Name: adfieldjson_field_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX adfieldjson_field_idx ON adfieldjson USING gin (field);

--
-- Name: idx_adfieldjson_formref; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_adfieldjson_formref ON adfieldjson USING btree (((field #>> '{formRef}'::text[])));


--
-- Name: idx_adfieldjson_formrefid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_adfieldjson_formrefid ON adfieldjson USING btree (formref);


--
-- Name: idx_adfieldjson_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_adfieldjson_type ON adfieldjson USING btree (fieldtype);

