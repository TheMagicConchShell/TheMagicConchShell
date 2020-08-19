import Vue from 'vue';
import VueI18n from 'vue-i18n';
Vue.use(VueI18n);

const messages = {
    ko: {
        title: {
            title: '마법의 싸피 고둥',
            counselBoard: '고민게시판',
            ranking: '랭킹',
            qna: '문의',
            notice: '공지사항',
            chatting: '채팅',
        },
        home: {
            thisWeek: '금주의 싸피고둥이들',
            message: '{count} 명의 싸피고둥이들이 울림을 기다리는중',
            history: '지난 대나무숲',
        },
        board: {
            no: '번호',
            title: '제목',
            views: '조회',
            writer: '작성자',
            date: '날짜',
            all: '전체',
        },
        layout: {
            login: '로그인',
            regist: '회원가입',
            profile: '프로필',
            logout: '로그아웃',
            manage: '관리',
        },
        chat: {
            title: '싸피고둥 채팅방',
            exit: '나가기',
            send: '작성',
            message: {
                enter: ' 님이 참가했습니다',
                leave: ' 님이 퇴장했습니다',
            },
        },
        ranking: {
            board: '랭킹 게시판',
            type:{
                user:'유저',
                counsel:'고민',
                reply:'답변'
            },
            table:{
                rank:'순위',
                nickname:'닉네임',
                counsel:'고민',
                reply:'답변',
                point:'포인트',
                level:'레벨',
                title:'제목',
                writer:'작성자',
                like:'추천수',
                views:'조회수',
                date:'작성날짜',
                cNumber:'고민 번호'
            }
        }
    },
    en: {
        title: {
            title: 'Magic SSAFY conch',
            counselBoard: 'Counsels',
            ranking: 'Ranking',
            qna: 'Q&A',
            notice: 'Notice',
            chatting: 'Chatting',
        },
        home: {
            thisWeek: 'SSAFY Conches of the Week',
            message: '{count} conches waiting for magical answers',
            history: 'Past Stories',
        },
        board: {
            no: 'no',
            title: 'title',
            views: 'views',
            writer: 'writer',
            date: 'date',
            all: 'All',
        },
        layout: {
            login: 'Sign In',
            regist: 'Sign Up',
            profile: 'Profile',
            logout: 'Sign Out',
            manage: 'Manage',
        },
        chat: {
            title: 'SSAFY Chat',
            exit: 'Exit',
            send: 'Send',
            message: {
                enter: ' entered',
                leave: ' leaved',
            },
        },
        ranking: {
            board: 'Ranking Board',
            type:{
                user:'USER',
                counsel:'COUNSEL',
                reply:'REPLY'
            },
            table:{
                rank:'Rank',
                nickname:'Nickname',
                counsel:'Counsel',
                reply:'Reply',
                point:'Point',
                level:'Level',
                title:'Title',
                writer:'Writer',
                like:'Like',
                views:'Views',
                date:'Date',
                cNumber:'Counsel No.'
            }
        }
    },
    ch: {
        title: {
            title: '神奇的 SSAFY 海螺',
            counselBoard: '擔心公告欄',
            ranking: '排行',
            qna: '問題',
            notice: '注意',
            chatting: '闲聊',
        },
        home: {
            thisWeek: '本週的 SSAFY 海螺',
            message: '{count} 個海螺等待答案',
            history: '過去的故事',
        },
        board: {
            no: '号码',
            title: '题目',
            views: '点击率',
            writer: '制定者',
            date: '日期',
            all: '全部的',
        },
        layout: {
            login: 'Sign In',
            regist: 'Sign Up',
            profile: 'Profile',
            logout: 'Sign Out',
            manage: 'Manage',
        },
        chat: {
            title: 'SSAFY 聊天室',
            exit: '出去',
            send: '传送',
            message: {
                enter: ' 进来了',
                leave: ' 出去了',
            },
        },
        ranking: {
            board: '排行 公告欄',
            type:{
                user:'用户',
                counsel:'擔心',
                reply:'回复'
            },
            table:{
                rank:'排行',
                nickname:'昵称',
                counsel:'擔心',
                reply:'回复',
                point:'得分',
                level:'级别',
                title:'题目',
                writer:'制定者',
                like:'喜欢',
                views:'点击率',
                date:'日期',
                cNumber:'擔心 编号'
            }
        }
    },
};

const i18n = new VueI18n({
    locale: 'ko',
    fallbackLocale: 'ko',
    messages,
});

export default i18n;