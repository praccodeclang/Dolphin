package com.taewon.dolphin;
public class UserData {
    /* 현재 어플을 사용하고있는 유저의 데이터로, 로그인 성공 시, 초기화 되며 앱 실행 중 하나만 존재해야합니다.*/
    /* 어플 사용 중 하나만 존재해아합니다.*/
    /* UserData 클래스 사용법 */
    /* UserData.getInstance().get ~~~ */
    /* UserData.getInstance().set ~~~ */
    /* 싱글톤 패턴*/
    private static UserData instance = new UserData();
    public static UserData getInstance(){
        return instance;
    }
    private UserData(){ }
    /* 싱글턴 패턴 end */

    private String userStudentCode;
    private String userID;
    private String userName;
    private String userMajor;
    private String userDept;
    private String userPhoneNum;

    public String getUserDeptNoticeUrl() {
        return userDeptNoticeUrl;
    }

    private String userDeptNoticeUrl;




    //getters, setters.
    public String getUserStudentCode() { return userStudentCode; }
    public void setUserStudentCode(String userStudentCode) { this.userStudentCode = userStudentCode; }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMajor() {
        return userMajor;
    }
    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public String getUserDept() {
        return userDept;
    }
    public void setUserDept(String userDept) {
        this.userDept = userDept;
        switch (userMajor)
        {
            case "IT응용기술학부":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/computer/index.php?pCode=noticeall";
                break;
            case "간호학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/nursing/?pCode=notice";
                break;
            case "유아교육과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/baby/?pCode=notice";
                break;
            case "스포츠지도학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/sports/?pCode=notice";
                break;
            case "건축디자인학부":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/design/?pCode=notice";
                break;
            case "물리치료학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/physical/?pCode=notice";
                break;
            case "사회복지학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/family/?pCode=notice";
                break;
            case "기계공학부":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/me/?pCode=deptboard";
                break;
            case "치위생학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/dental/?pCode=notice";
                break;
            case "세무회계학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/account/?pCode=notice";
                break;
            case "전기전자공학부":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/electric/?pCode=notice";
                break;
            case "식품영양학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/fn/?pCode=notice";
                break;
            case "유통물류경영학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/flow/?pCode=notice";
                break;
            case "안전 및 산업공학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/industry/?pCode=notice";
                break;
            case "호텔조리제빵과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/hotel/?pCode=notice";
                break;
            case "글로벌비즈니스학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/global/index.php?pCode=notice";
                break;
            case "디지털콘텐츠디자인학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/cdesign/?pCode=notice";
                break;
            case "화학공학과":
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/chemical/?pCode=notice";
                break;
            default:
                this.userDeptNoticeUrl = "https://www.uc.ac.kr/www/index.php?pCode=ucnotice";
                break;
        }

    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }
    public void setUserPhoneNum(String userPhoneNum) { this.userPhoneNum = userPhoneNum; }
}
