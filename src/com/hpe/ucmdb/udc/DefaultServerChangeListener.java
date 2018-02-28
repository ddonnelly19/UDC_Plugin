package com.hpe.ucmdb.udc;

import com.intellij.util.messages.Topic;

interface DefaultServerChangeListener {
    Topic<DefaultServerChangeListener> TOPIC = Topic.create(DefaultServerChangeListener.class.getName(), (Class<DefaultServerChangeListener>) DefaultServerChangeListener.class);

    void defaultServerChanged();
}