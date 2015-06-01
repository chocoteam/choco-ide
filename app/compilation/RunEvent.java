package compilation;

/**
 * Created by yann on 26/05/15.
 */
public class RunEvent {

    private final String message;
    private final String kind;
    private final int delay;

    public String getMessage() {
        return message;
    }

    public String getKind() {
        return kind;
    }

    public int getDelay() {
        return delay;
    }

    public enum Kind{
        OUT("stdout"),
        ERR("stderr");

        private final String desc;

        Kind(String desc) {
            this.desc = desc;
        }

        public String toString(){
            return this.desc;
        }
    }


    public RunEvent(String message, String kind, int delay){
        this.message = message;
        this.kind = kind;
        this.delay = delay;
    }


}
