package life.ls.community.enums;

/**
 * 回复的类型
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    //判断回复的类型是否存在
    public static boolean isExist(Integer type) {
        //遍历
        for(CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if (commentTypeEnum.getType()==type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
