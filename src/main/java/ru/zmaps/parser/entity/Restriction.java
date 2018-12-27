package ru.zmaps.parser.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restriction {
    Long from;
    Long to;
    Long via;
    RestrictionType type;

    public static Restriction convertRelation(Relation rel) {
        Restriction restriction = new Restriction();

        if (rel.getMembers().containsKey("from")) restriction.setFrom(rel.getMember("from"));
        if (rel.getMembers().containsKey("to")) restriction.setTo(rel.getMember("to"));
        if (rel.getMembers().containsKey("via")) restriction.setVia(rel.getMember("via"));

        String rr = rel.tags.get("restriction");

        if (rr == null || restriction.getFrom() == null || restriction.getTo() == null || restriction.getVia() == null) return null;

        restriction.type = RestrictionType.convert(rr);

        return restriction;
    }
}
