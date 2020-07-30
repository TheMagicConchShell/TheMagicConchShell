<template>
    <div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                금주의 싸피고둥이들
            </div>
        </div>
        <!-- 금주 -->
        <div
            id="bloglist"
            class="d-flex"
        >
            <div id="mainpost">
                asdf
            </div>
            <div id="sidepost">
                <b-jumbotron
                    fluid
                    lead="연애가 하고 싶어요.."
                    class="w-100 m-0 border"
                >
                    <p>written by 연애고수</p>
                </b-jumbotron>
                <b-jumbotron
                    fluid
                    lead="취업 고민ㅠㅠ"
                    class="w-100 m-0"
                >
                    <p>written by 30대 무직 남성</p>
                </b-jumbotron>
                <b-jumbotron
                    lead="이 코인 존버가 답일까요?"
                    class="w-100"
                >
                    <p>written by 개미</p>
                </b-jumbotron>
                <infinite-loading @infinite="infiniteHandler" />
            </div>
        </div>

        <div id="home">
            <div class="d-flex flex-column-reverse">
                지난 대나무숲
            </div>
        </div>

        <!-- 지난주 -->
        <div
            id="bloglist"
            class="cursor"
        >
            <b-card
                img-src="https://placekitten.com/300/300"
                img-alt="Card image"
                img-left
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        답변들
                    </div>
                    
                </b-card-text>
            </b-card>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'Home',
    data() {
        return {
            nomouse: true,
            /* 무한 스크롤 데이터 */
            page: 1,
            list: [],
        };
    },
    methods: {
        mouseover() {
            this.nomouse = false;
        },
        mouseout() {
            this.nomouse = true;
        },
        /* 무한스크롤 메서드 */
        infiniteHandler($state) {
            axios.get('', {
                params: {
                    page: this.page,
                },
            })
                .then(({ data }) => {
                    if (data.hits.length) {
                        this.page += 1;
                        this.list.push(...data.hits);
                        $state.loaded();
                    } else {
                        $state.complete();
                    }
                });
        },
    },
};
</script>

<style scoped>
#home {
  display: flex;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 30px;
}
#bigbutton {
  color: white;
  padding: 10px;
  background: linear-gradient( 0, #9C8F96, #ccbbc4);
  width: 20%;
  border-radius: 10px;
  text-decoration: none;
}
#click {
  animation: motion 0.5s linear 0s infinite alternate;
}

#bloglist {
    width: 100%;
    height: 70vh;
    border: 1px solid #b6b6b6;
    border-radius: 5px;
}
#mainpost {
    width: 70%;
    height: 100%;
}
#sidepost {
    width: 30%;
    height: 100%;
    overflow: auto;
}
#slide {
    max-height: auto;
    max-width: auto;
}
#answerheader {
    height: 100px;
}
#slide {
    border: 1px solid;
    width: 50%;
}
</style>
