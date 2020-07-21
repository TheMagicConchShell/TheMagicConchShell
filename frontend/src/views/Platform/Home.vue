<template>
    <div class="home">
        <div class="bigbutton">
            <router-link
                :to="{name: 'Blog'}"
                class="text-decoration-none text-light"
            >
                블로그 개설
            </router-link>
        </div>
        <div class="bigbutton additional">
            임의의 공간
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
.home {
  display: flex;
}
.bigbutton {
  margin: 10px;
  color: white;
  background-color: firebrick;
  width: 50%;
  border-radius: 10px;
  font-size: 75px;
  text-decoration: none;
}
.additional {
  background-color: #ff7300;
}
</style>
