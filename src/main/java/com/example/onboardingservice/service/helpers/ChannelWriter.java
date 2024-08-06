package com.example.onboardingservice.service.helpers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelWriter {
    private static final int TRANSFER_SIZE = 8192;

    private final boolean closeTargetOnCompletion;
    private final boolean closeSourceOnCompletion;

    private final ByteBuffer buffer = ByteBuffer.allocate(TRANSFER_SIZE);

    public long write(WritableByteChannel target, ReadableByteChannel source) throws IOException {
        buffer.clear();

        long bytesWritten = 0;
        while (source.read(buffer) > 0) {
            buffer.flip();
            bytesWritten += target.write(buffer);
            buffer.clear();
        }

        closeIfRequested(target, source);
        return bytesWritten;
    }

    private void closeIfRequested(WritableByteChannel target, ReadableByteChannel source) throws IOException {
        if (closeTargetOnCompletion) {
            target.close();
        }
        if (closeSourceOnCompletion) {
            source.close();
        }
    }

    public static WriterBuilder builder() {
        return new WriterBuilder();
    }

    @RequiredArgsConstructor
    public static class WriterBuilder {
        private boolean closeTargetOnCompletion = true;
        private boolean closeSourceOnCompletion = true;

        public WriterBuilder setCloseSourceOnCompletion(boolean closeSourceOnCompletion) {
            this.closeSourceOnCompletion = closeSourceOnCompletion;
            return this;
        }

        public WriterBuilder setCloseTargetOnCompletion(boolean closeTargetOnCompletion) {
            this.closeTargetOnCompletion = closeTargetOnCompletion;
            return this;
        }

        public ChannelWriter build() {
            return new ChannelWriter(closeTargetOnCompletion, closeSourceOnCompletion);
        }
    }
}
