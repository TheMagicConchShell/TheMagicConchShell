<template>
    <div id="platform">
        <div id="home">
            <div class="d-flex flex-column-reverse">
                실시간 활동 블로그
            </div>

            <div
                id="bigbutton"
                @mouseover="mouseover()"
                @mouseout="mouseout()"
            >
                <router-link
                    :to="{name: 'Open'}"
                    class="text-decoration-none text-light"
                >
                    내 블로그 만들기
                </router-link>
            </div>
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
    </div>
</template>

<script>
export default {
    name: 'Home',
    data() {
        return {
            nomouse: true,
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
  background-color: firebrick;
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
