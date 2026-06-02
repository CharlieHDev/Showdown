package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;

public class DashTimeData {

        private final String player;

        private long lap1Timestamp;
        private long lap2Timestamp;
        private long lap3Timestamp;


        private final Showdown2 plugin;

        public DashTimeData(String player, Showdown2 plugin) {
            this.player = player;
            this.plugin = plugin;
        }

        public String getPlayer() {
            return player;
        }

        public void setlap1Time() {
            lap1Timestamp = System.currentTimeMillis();
        }

        public void setlap2Time() {
            lap2Timestamp = System.currentTimeMillis();
        }

        public void setlap3Time() {
            lap3Timestamp = System.currentTimeMillis();
        }

        public long getLap1Time() {
            return lap1Timestamp - plugin.ddStartTime;
        }

        public long getLap2Time() {
            return lap2Timestamp - lap1Timestamp;
        }

        public long getLap3Time() {
            return lap3Timestamp - lap2Timestamp;
        }

        public long getLap1Timestamp() {
            return lap1Timestamp;
        }

        public long getLap2Timestamp() {
            return lap2Timestamp;
        }

        public long getLap3Timestamp() {
            return lap3Timestamp;
        }

        public String getLap1TimeConverted() {
            if (lap1Timestamp == 0) return "N/A";
            return formatTime(getLap1Time());
        }

        public String getLap2TimeConverted() {
            if (lap2Timestamp == 0) return "N/A";
            return formatTime(getLap2Time());
        }

        public String getLap3TimeConverted() {
            if (lap3Timestamp == 0) return "N/A";
            return formatTime(getLap3Time());
        }

    public String getFinalCompletionTimeConverted() {

        if (lap1Timestamp == 0 || lap2Timestamp == 0 || lap3Timestamp == 0) {
            return "N/A";
        }

        long totalTime = getLap1Time() + getLap2Time() + getLap3Time();

        return formatTime(totalTime);
    }


        private static String formatTime(long millis) {

            long minutes = millis / 60000;
            long seconds = (millis % 60000) / 1000;
            long milliseconds = millis % 1000;

            return String.format("%02d:%02d:%03d",
                    minutes,
                    seconds,
                    milliseconds
            );
        }
    }

