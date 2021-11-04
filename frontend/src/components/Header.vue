<template>
  <div class="bg-headerGray h-16 w-full grid grid-cols-2">
    <div class="h-16 ml-2 flex">
      <div
        class="
          grid
          justify-items-center
          items-center
          h-full
          w-auto
          ml-3
          cursor-pointer
          mr-10
        "
        @click="goMain()"
      >
        <img src="@/images/logo.png" class="h-12 w-auto" />
      </div>
      <div
        v-if="projectName"
        class="sm:flex justify-center items-center text-white text-2xl hidden"
      >
        {{ projectName }}
      </div>
    </div>
    <div class="grid justify-items-end items-center h-16 mr-5">
      <div class="grid justify-items-center grid-cols-2 h-full">
        <div
          class="
            grid
            justify-items-center
            items-center
            relative
            h-full
            w-16
            cursor-pointer
            mr-2
            hover:opacity-50
          "
          @click="goAlarm()"
        >
          <i class="fas fa-bell text-white text-lg" />
          <div
            class="
              grid
              justify-items-center
              items-center
              absolute
              right-3
              bottom-4
              h-4
              w-4
              rounded-full
              bg-red-600
              text-white text-xs
              font-bold
            "
            v-if="this.cnt != ''"
          >
            {{this.cnt}}
          </div>
        </div>

        <div
          class="
            h-full
            w-16
            grid
            justify-items-center
            items-center
            cursor-pointer
            hover:opacity-50
          "
          @click="goProfile()"
        >
          <i class="fas fa-user-circle text-white text-3xl"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";

export default {
  name: "Header",
  data() {
    return {
      showAlert: false,
      alarmList: [],
      cnt: 0,
    };
  },
  components: {},
  created() {
    this.connect();
  },
  methods: {
    connect() {
      const serverURL = "http://localhost:8082/alarm";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, { debug: false });
      this.stompClient.connect({}, () => {
        // 소켓 연결 성공
        this.connected = true;
        this.stompClient.debug = () => {};
        this.stompClient.send(
          "/server/getAlarm",
          JSON.stringify({
            memberId: this.id,
          }),
          {}
        );

        this.stompClient.subscribe("/client/alarm/" + this.id, (res) => {
          
          this.alarmList = JSON.parse(res.body);
          
          var alarmCnt = this.alarmList.length;
          if(alarmCnt > 9) this.cnt = "9+";
          else if(alarmCnt == 0) this.cnt = "";
          else this.cnt = alarmCnt;

        });

      });
    },
    goMain() {
      this.$router.push("/");
    },
    goAlarm() {
      this.$router.push({name:"Alarm", params: { alarmList: this.alarmList } });
    },
    goProfile() {
      this.$router.push("/profile");
    },
  },
  computed: {
    ...mapGetters(["projectName", "id"]),
  },
};
</script>
