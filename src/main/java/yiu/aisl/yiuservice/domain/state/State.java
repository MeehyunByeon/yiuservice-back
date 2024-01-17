package yiu.aisl.yiuservice.domain.state;

public interface State {
    int getState();

//    public static State fromInt(int value) {
//        for (PostState postState : PostState.values()) {
//            if (postState.getState() == value) {
//                return postState;
//            }
//        }
//        throw new IllegalArgumentException("Invalid PostState value: " + value);
//    }
}