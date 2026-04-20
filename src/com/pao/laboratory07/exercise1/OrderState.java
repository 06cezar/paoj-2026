package com.pao.laboratory07.exercise1;

public enum OrderState {
    PLACED {
        @Override
        public OrderState next() {
            return PROCESSED;
        }
    },
    PROCESSED {
        @Override
        public OrderState next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() {
            return null; // final state
        }
    },
    CANCELED {
        @Override
        public OrderState next() {
            return null; // final state
        }
    };

    public abstract OrderState next();

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELED;
    }
}
