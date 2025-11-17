public class SlidingWindowProtoco {
    private final int windowSize;
    private int sendBase;
    private int nextSeqNum;

    public Swp(int windowSize) {
        this.windowSize = windowSize;
        this.sendBase = 0;
        this.nextSeqNum = 0;
    }

    public void sendData(char[] data) {
        while (nextSeqNum < sendBase + windowSize && nextSeqNum < data.length) {
            Packet packet = createPacket(data[nextSeqNum]);
            simulateSendPacket(packet);
            nextSeqNum++;
        }
    }

    private void receiveAck(int ackNum) {
        sendBase = ackNum + 1;
    }

    private Packet createPacket(char data) {
        return new Packet(nextSeqNum, data);
    }

    private void simulateSendPacket(Packet packet) {
        System.out.println("Sending packet = " + packet);
    }
    public void simulateAckPacket(int ackNum, char[] data) {
       System.out.println("Received ACK for packet " + ackNum);
       receiveAck(ackNum);
       sendData(data); 
    }


    private static class Packet {
        private final int seqNum;
        private final char data;

        public Packet(int seqNum, char data) {
            this.seqNum = seqNum;
            this.data = data;
        }
        public String toString() {
            return "Packet{seqNum=" + seqNum + ", data=" + data + "}";
        }
    }

    public static void main(String[] args) {
     char[] data = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
     Swp swp = new Swp(7);
     swp.sendData(data);
     swp.simulateAckPacket(0, data);
     swp.simulateAckPacket(1, data);
     swp.simulateAckPacket(2, data);
     swp.simulateAckPacket(3, data);
    }
}
