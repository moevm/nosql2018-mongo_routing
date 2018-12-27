package ru.zmaps.parser.entity;

public enum RestrictionType {
    NO_RIGHT_TURN, NO_LEFT_TURN, NO_U_TURN, NO_STRAIGHT_ON, ONLY_RIGHT_TURN, ONLY_LEFT_TURN, ONLY_U_TURN, ONLY_STRAIGHT_ON, NO_ENTRY, NO_EXIT;

    static RestrictionType convert(String rType) {
        switch (rType) {
            case "no_right_turn":
                return NO_RIGHT_TURN;
            case "no_left_turn":
                return NO_LEFT_TURN;
            case "no_u_turn":
                return NO_U_TURN;
            case "no_straight_on":
                return NO_STRAIGHT_ON;
            case "only_right_turn":
                return ONLY_RIGHT_TURN;
            case "only_left_turn":
                return ONLY_LEFT_TURN;
            case "only_straight_on":
                return ONLY_STRAIGHT_ON;
            case "no_entry":
                return NO_ENTRY;
            case "only_u_turn":
                return ONLY_U_TURN;
            case "no_exit":
                return NO_EXIT;
        }

        throw new IllegalArgumentException("Can't parse restriction type: " + rType);
    }
}

