<template>
  <div class="bg-headerGray h-16 grid grid-cols-2">
    <div class="h-16 ml-2 flex">
      <div class="flex h-full w-auto ml-3 cursor-pointer" @click="goMain()">
        <img src="@/images/logo.png" />
      </div>
      <div v-if="projectName" class="flex justify-center items-center text-white text-2xl">
        {{ projectName }}
      </div>
    </div>
    <div class="grid justify-items-end items-center h-16 mr-10">
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
            mr-4
            hover:opacity-50
          "
          @click="goAlert()"
        >
          <i class="fas fa-bell text-white text-3xl" />
          <div
            class="
              grid
              justify-items-center
              items-center
              absolute
              right-0
              bottom-2
              h-6
              w-6
              rounded-full
              bg-red-600
              text-white text-xs
              font-bold
            "
          >
            1
          </div>
        </div>

        <div
          class="h-full w-16 grid justify-items-center items-center cursor-pointer hover:opacity-50"
          @click="goProfile()"
        >
          <i class="fas fa-user-circle text-white text-5xl"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: 'Header',
  data() {
    return {
      showAlert: false,
    };
  },
  components: {},
  methods: {
    goMain() {
      this.$router.push('/');
    },
    goAlert() {
      this.$router.push('/alert');
    },
    goProfile() {
      this.$router.push('/profile');
    },
  },
  computed: {
    ...mapGetters(['projectName']),
  },
};
</script>
