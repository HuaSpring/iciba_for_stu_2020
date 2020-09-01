package com.fspt.practice.bean;

import java.util.List;

public class Fanyi_Eng_T_CN_Bean {


    /**
     * status : 0
     * content : {"ph_en":"ˈæpl","ph_am":"ˈæpəl","ph_en_mp3":"http://res.iciba.com/resource/amp3/oxford/0/44/29/44297283b5e4b5b0a991213897f0b14a.mp3","ph_am_mp3":"http://res.iciba.com/resource/amp3/1/0/1f/38/1f3870be274f6c49b3e31a0c6728957f.mp3","ph_tts_mp3":"http://res-tts.iciba.com/1/f/3/1f3870be274f6c49b3e31a0c6728957f.mp3","word_mean":["n. 苹果;苹果树;苹果公司;"]}
     */

    private int status;
    private ContentBean content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * ph_en : ˈæpl
         * ph_am : ˈæpəl
         * ph_en_mp3 : http://res.iciba.com/resource/amp3/oxford/0/44/29/44297283b5e4b5b0a991213897f0b14a.mp3
         * ph_am_mp3 : http://res.iciba.com/resource/amp3/1/0/1f/38/1f3870be274f6c49b3e31a0c6728957f.mp3
         * ph_tts_mp3 : http://res-tts.iciba.com/1/f/3/1f3870be274f6c49b3e31a0c6728957f.mp3
         * word_mean : ["n. 苹果;苹果树;苹果公司;"]
         */

        private String ph_en;
        private String ph_am;
        private String ph_en_mp3;
        private String ph_am_mp3;
        private String ph_tts_mp3;
        private List<String> word_mean;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<String> getWord_mean() {
            return word_mean;
        }

        public void setWord_mean(List<String> word_mean) {
            this.word_mean = word_mean;
        }
    }
}
