<template>
  <div id="app" class="h-screen bg-contentGray">
    <router-view />
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "App",
  data(){
    return{
      cnt: '',
    }
  },
  created() {
    this.connect();
  },
  computed:{
    ...mapGetters(['id'])
  },
  methods:{
    ...mapActions(['set_totalAlarmCnt', 'set_stomp']),
    connect() {
      const serverURL = 'http://localhost:8082/socket';
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect({}, () => {

        this.set_stomp(this.stompClient);
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
  beforeDestroy(){
    this.set_stomp(null);
    this.stompClient.disconnect();
  }
};
</script>
