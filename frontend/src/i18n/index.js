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
        },
        account: {
            delete: {
                done: '안내',
                message: '탈퇴하시겠습니까?',
            },
            level: {
                message: '포인트 {point}를 소모하여 Lv.{dest}로 레벨업 합니다.',
                up: '레벨업',
            },
            post: {
                origin: '원글',
                selected: '채택',
                noselected: '미채택',
            },
            info: {
                email: '이메일',
                nickname: '별명',
                password: '비밀번호',
                check: '비밀번호 확인',
                holder: {
                    password: '영문/숫자 포함 8자 이상',
                },
                check: '비밀번호 확인',
                point: {
                    name: '포인트',
                    current: '현재 보유 포인트',
                },
                level: '레벨',
                update: '정보 수정',
                delete: '회원 탈퇴'
            },
            nocontent: {
                post: '고민 글 작성 내역이 없습니다.',
                reply: '답변 글 작성 내역이 없습니다.',
                point: '포인트 사용 내역이 없습니다.',
            },
            titles: {
                post: '내 고민 글 목록',
                reply: '내 답변 목록',
                point: '포인트 내역',
            },
        },
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
            },
        },
        account: {
            delete: {
                done: 'Message',
                message: 'Are you sure you want to delete the account?',
            },
            post: {
                nocontent: 'There is no history of writing your concerns.',
                origin: 'Origin',
                selected: 'Selected',
                noselected: 'Not Selected',
            },
            info: {
                email: 'Email',
                nickname: 'Nickname',
                password: 'Password',
                check: 'Password check',
                holder: {
                    password: '8 characters cotain both Alphabet and Numeric',
                }, 
                point: {
                    name: 'Point',
                    current: 'Current Point',
                },
                level: 'Level',
                update: 'Update',
                delete: 'Delete Account'
            },
            level: {
                message: 'Level up to Level {dest} by consuming Point {point}.',
                up: 'Level Up',
            },
            nocontent: {
                post: 'There is no history of concern.',
                reply: 'There is no history of answer.',
                point: 'There is no history of point.',
            },
            titles: {
                post: 'My Concerns',
                reply: 'My Answers',
                point: 'Point History',
            },
        },
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
        },
        account: {
            delete: {
                done: '起源',
                message: '确定要删除账户吗?',
            },
            post: {
                nocontent: '没有写你的担忧的历史。',
                origin: '起源',
                selected: '采纳',
                noselected: '未采纳',
            },
            info: {
                email: '电邮',
                nickname: '尼克名',
                password: '密码',
                check: '密码确认',
                holder: {
                    password: '字母和数字的8个字符',
                }, 
                point: {
                    name: '分数',
                    current: '现分数',
                },
                level: '水平仪',
                update: '修订',
                delete: '退出'
            },
            level: {
                message: '驾驶人可使用 {point} 点, 把车速提高至 {dest} 点',
                up: '提升水平',
            },
            nocontent: {
                post: '没有令人担心的历史。',
                reply: '没有答案的历史。',
                point: '没有一点历史意义。',
            },
            titles: {
                post: '我的担心',
                reply: '我的答案',
                point: '观点历史',
            },
        },
    },
};

const i18n = new VueI18n({
    locale: 'ko',
    fallbackLocale: 'ko',
    messages,
});

export default i18n;