<template>
    <div class="home">
        <Main />
        <img
            alt="Vue logo"
            src="../assets/logo.png"
        >
        <HelloWorld msg="Welcome to Your Vue.js App" />
    </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue';
import Main from '@/views/Main.vue';

export default {
    name: 'Home',
    components: {
        HelloWorld,
        Main,
    },
};
</script>
