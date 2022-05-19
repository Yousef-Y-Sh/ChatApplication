package com.yousef.sh.chatapplication.Utils;

import com.yousef.sh.chatapplication.ByteBuf;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}