<template>
    <div>
        <div id="home">
            <transition name="conversion">
                <span :key="language">{{ $t('title.notice') }}</span>
            </transition>
        </div>
        <router-view />
    </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';

export default {
    name: 'NoticeView',
    computed: {
        ...mapGetters(['language'])
    }
};
</script>

<style scoped>
#home {
  display: flex;
  font-family: sb;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 30px;
}
</style>