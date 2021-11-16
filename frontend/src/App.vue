<template>
  <div id="app" class="h-screen bg-contentGray">
    <router-view :stomp="stompClient" />
    <Loading v-if="isLoading" />
  </div>
</template>

<script>
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import Loading from '@/components/Loading.vue';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'App',
  components: {
    Loading,
  },
  data() {
    return {
      cnt: '',
    };
  },
  created() {
    this.connect();
  },
  computed: {
    ...mapGetters(['id', 'isLoading']),
  },
  methods: {
    ...mapActions(['set_totalAlarmCnt']),
    connect() {
      const serverURL = 'http://localhost:8082/socket';
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect({}, () => {
        // 소켓 연결 성공
        this.connected = true;
        this.stompClient.debug = () => {};
        this.stompClient.send(
          '/server/getAlarm',
          JSON.stringify({
            memberId: this.id,
          }),
          {}
        );

        this.stompClient.subscribe('/client/alarm/' + this.id, (res) => {
          this.alarmList = JSON.parse(res.body);
          this.set_totalAlarmCnt(this.alarmList.length);
        });
      });
    },
  },
};
</script>
