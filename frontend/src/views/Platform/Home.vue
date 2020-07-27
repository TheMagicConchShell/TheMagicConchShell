<template>
    <div>
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
                    <carousel
                        :per-page="1"
                        :navigate-to="someLocalProperty"
                        :mouse-drag="true"
                    >
                        <slide>
                            Slide 1 Content
                            <hr>
                            asdfasdf
                        </slide>
                        <slide>
                            Slide 2 Content
                            <hr>
                            qwesdf
                        </slide>
                        <slide>
                            Slide 3 Content
                            <hr>
                            qwerty
                        </slide>
                    </carousel>
                    <!-- 무한 스크롤-->
                    <infinite-loading @infinite="infiniteHandler" />
                </b-card-text>
            </b-card>
        </div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                금주의 싸피고둥이들
            </div>
            <div
                v-show="nomouse"
                id="click"
            >
                <i
                    class="far fa-hand-point-up"
                    style="color: black"
                /> click!
            </div>
            <router-link
                id="bigbutton"
                :to="{name: 'Open'}"
                class="text-decoration-none text-light"
                @mouseover="mouseover()"
                @mouseout="mouseout()"
            >
                이번 주 고민 보러가기
            </router-link>
        </div>
        <!-- 금주 -->
        <div
            id="bloglist"
            class="cursor"
        >
            <b-card
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        답변들
                    </div>
                    <carousel
                        :per-page="1"
                        :navigate-to="someLocalProperty"
                        :mouse-drag="true"
                    >
                        <slide>
                            Slide 1 Content
                        </slide>
                        <slide>
                            Slide 2 Content
                        </slide>
                    </carousel>
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
  background-color: #978F96;
  width: 20%;
  border-radius: 10px;
  text-decoration: none;
}
#click {
  animation: motion 0.5s linear 0s infinite alternate;
}

#bloglist {
  margin-top: 30px;
  height: 30vh;
  overflow: auto;
}
#answerheader {
    height: 100px;
}
</style>
