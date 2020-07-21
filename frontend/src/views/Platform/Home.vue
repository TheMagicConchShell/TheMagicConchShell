<template>
    <div id="platform">
        <div id="home">
            <div id="present">
                실시간 활동 블로그
            </div>

            <div id="bigbutton">
                <router-link
                    :to="{name: 'Blog'}"
                    class="text-decoration-none text-light"
                >
                    내 블로그 만들기
                </router-link>
            </div>
        </div>

        <div id="bloglist">
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
    components: {

    },
};
</script>

<style scoped>
#home {
  display: flex;
  justify-content: space-between;
  font-size: 30px;
}
#present {
  vertical-align: bottom;
}
#bigbutton {
  color: white;
  padding: 10px;
  background-color: firebrick;
  width: 20%;
  border-radius: 10px;
  text-decoration: none;
}

#bloglist {
  margin-top: 30px;
}
</style>
