<template>
    <div id="content">
        <div id="home">
            <div class="d-flex flex-column-reverse">
                실시간 활동 블로그
            </div>
            <router-link
                id="bigbutton"
                :to="{name: 'Open'}"
                class="text-decoration-none text-light"
                @mouseover="mouseover()"
                @mouseout="mouseout()"
            >
                내 블로그 만들기
            </router-link>
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

        <!-- 블로그 목록 -->
        <div
            id="bloglist"
            class="cursor"
            @click="$router.push({name: 'Blog'})"
        >
            <b-card
                img-src="https://placekitten.com/300/300"
                img-alt="Card image"
                img-left
                class="mb-3"
            >
                <b-card-text>
                    귀여운 고양이 블로그
                </b-card-text>
            </b-card>
        </div>
        <!-- 무한 스크롤-->
        <infinite-loading @infinite="infiniteHandler" />
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
    watch: {

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
  position: absolute;
  right: 20px;
  animation: motion 0.5s linear 0s infinite alternate;
}

#bloglist {
  margin-top: 30px;
}
</style>
