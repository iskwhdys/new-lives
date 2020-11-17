CREATE MATERIALIZED VIEW public.nijisanji_liver
TABLESPACE pg_default
AS SELECT liver_tag.id,
    liver_tag.value AS youtube
   FROM liver_tag
  WHERE liver_tag.key::text = 'youtube'::text
UNION
 SELECT liver.id,
    liver.youtube
   FROM liver
  WHERE liver.company::text = 'いちから株式会社'::text AND liver."group"::text = 'にじさんじ'::text
WITH DATA;