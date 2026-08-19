--
-- PostgreSQL database dump
--

\restrict OcB2rSqhLVeWqQHWZtnciEPUIHcI4ORwwgplqciKYS6WNXBiRn7wf5dEBWt5g2u

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

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
-- Name: achat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.achat (
    num_achat character varying(50) NOT NULL,
    num_prod character varying(50) NOT NULL,
    nom_client character varying(100) NOT NULL,
    nbr_litre integer NOT NULL,
    montant_paye_achat integer NOT NULL,
    date_achat timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT achat_montant_paye_achat_check CHECK ((montant_paye_achat >= 0)),
    CONSTRAINT achat_nbr_litre_check CHECK ((nbr_litre > 0))
);


ALTER TABLE public.achat OWNER TO postgres;

--
-- Name: details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.details (
    num_entr character varying(50) NOT NULL,
    num_serv character varying(50) NOT NULL,
    prix_applique integer NOT NULL,
    CONSTRAINT details_prix_applique_check CHECK ((prix_applique >= 0))
);


ALTER TABLE public.details OWNER TO postgres;

--
-- Name: entree; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.entree (
    num_entree character varying(50) NOT NULL,
    num_prod character varying(50) NOT NULL,
    stock_entree integer NOT NULL,
    date_entree timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT entree_stock_entree_check CHECK ((stock_entree > 0))
);


ALTER TABLE public.entree OWNER TO postgres;

--
-- Name: entretien; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.entretien (
    num_entr character varying(50) NOT NULL,
    immatriculation_voiture character varying(20) NOT NULL,
    nom_client character varying(100) NOT NULL,
    date_entretien timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    prix_entretien integer DEFAULT 0,
    CONSTRAINT entretien_prix_entretien_check CHECK ((prix_entretien >= 0))
);


ALTER TABLE public.entretien OWNER TO postgres;

--
-- Name: produit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produit (
    num_prod character varying(50) NOT NULL,
    design character varying(50) NOT NULL,
    stock integer DEFAULT 0,
    prix_litre_prod integer NOT NULL,
    CONSTRAINT produit_prix_litre_prod_check CHECK ((prix_litre_prod >= 0)),
    CONSTRAINT produit_stock_check CHECK ((stock >= 0))
);


ALTER TABLE public.produit OWNER TO postgres;

--
-- Name: service; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service (
    num_serv character varying(50) NOT NULL,
    service character varying(50) NOT NULL,
    prix_service integer NOT NULL,
    CONSTRAINT service_prix_service_check CHECK ((prix_service >= 0))
);


ALTER TABLE public.service OWNER TO postgres;

--
-- Data for Name: achat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.achat (num_achat, num_prod, nom_client, nbr_litre, montant_paye_achat, date_achat) FROM stdin;
\.


--
-- Data for Name: details; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.details (num_entr, num_serv, prix_applique) FROM stdin;
\.


--
-- Data for Name: entree; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.entree (num_entree, num_prod, stock_entree, date_entree) FROM stdin;
\.


--
-- Data for Name: entretien; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.entretien (num_entr, immatriculation_voiture, nom_client, date_entretien, prix_entretien) FROM stdin;
\.


--
-- Data for Name: produit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.produit (num_prod, design, stock, prix_litre_prod) FROM stdin;
\.


--
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.service (num_serv, service, prix_service) FROM stdin;
\.


--
-- Name: achat achat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.achat
    ADD CONSTRAINT achat_pkey PRIMARY KEY (num_achat);


--
-- Name: entree entree_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entree
    ADD CONSTRAINT entree_pkey PRIMARY KEY (num_entree);


--
-- Name: entretien entretien_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entretien
    ADD CONSTRAINT entretien_pkey PRIMARY KEY (num_entr);


--
-- Name: details pk_details; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.details
    ADD CONSTRAINT pk_details PRIMARY KEY (num_entr, num_serv);


--
-- Name: produit produit_design_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produit
    ADD CONSTRAINT produit_design_key UNIQUE (design);


--
-- Name: produit produit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produit
    ADD CONSTRAINT produit_pkey PRIMARY KEY (num_prod);


--
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (num_serv);


--
-- Name: service service_service_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_service_key UNIQUE (service);


--
-- Name: achat fk_achat_produit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.achat
    ADD CONSTRAINT fk_achat_produit FOREIGN KEY (num_prod) REFERENCES public.produit(num_prod) ON DELETE RESTRICT;


--
-- Name: details fk_details_entretien; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.details
    ADD CONSTRAINT fk_details_entretien FOREIGN KEY (num_entr) REFERENCES public.entretien(num_entr) ON DELETE CASCADE;


--
-- Name: details fk_details_service; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.details
    ADD CONSTRAINT fk_details_service FOREIGN KEY (num_serv) REFERENCES public.service(num_serv) ON DELETE RESTRICT;


--
-- Name: entree fk_entree_produit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.entree
    ADD CONSTRAINT fk_entree_produit FOREIGN KEY (num_prod) REFERENCES public.produit(num_prod) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

\unrestrict OcB2rSqhLVeWqQHWZtnciEPUIHcI4ORwwgplqciKYS6WNXBiRn7wf5dEBWt5g2u

