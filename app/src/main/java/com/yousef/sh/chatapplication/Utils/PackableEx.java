package com.yousef.sh.chatapplication.Utils;

import com.yousef.sh.chatapplication.ByteBuf;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
