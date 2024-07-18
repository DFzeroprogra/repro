const audioPlayer = document.getElementById('audio-player');
const playPauseBtn = document.getElementById('play-pause');
const prevBtn = document.getElementById('prev');
const nextBtn = document.getElementById('next');
const progressBar = document.getElementById('progress');
const volumeControl = document.getElementById('volume');
const muteUnmuteBtn = document.getElementById('mute-unmute');
const trackTitle = document.getElementById('track-title');
const fileInput = document.getElementById('file-input');
const youtubeUrlInput = document.getElementById('youtube-url');
const addYoutubeBtn = document.getElementById('add-youtube');

let isPlaying = false;
let currentTrackIndex = 0;
const tracks = [];

function loadTrack(index) {
    if (tracks[index].type === 'file') {
        audioPlayer.src = URL.createObjectURL(tracks[index].file);
    } else if (tracks[index].type === 'youtube') {
        // Para integrar la API de YouTube, necesitarás configurar y cargar el iframe API de YouTube
        loadYouTubeVideo(tracks[index].id);
    }
    trackTitle.textContent = tracks[index].title;
    audioPlayer.load();
}

function togglePlayPause() {
    if (isPlaying) {
        audioPlayer.pause();
        playPauseBtn.textContent = '▶️';
    } else {
        audioPlayer.play();
        playPauseBtn.textContent = '⏸️';
    }
    isPlaying = !isPlaying;
}

function playNextTrack() {
    currentTrackIndex = (currentTrackIndex + 1) % tracks.length;
    loadTrack(currentTrackIndex);
    if (isPlaying) {
        audioPlayer.play();
    }
}

function playPrevTrack() {
    currentTrackIndex = (currentTrackIndex - 1 + tracks.length) % tracks.length;
    loadTrack(currentTrackIndex);
    if (isPlaying) {
        audioPlayer.play();
    }
}

function updateProgressBar() {
    const progress = (audioPlayer.currentTime / audioPlayer.duration) * 100;
    progressBar.value = progress;
}

function seekTrack() {
    const seekTime = (progressBar.value / 100) * audioPlayer.duration;
    audioPlayer.currentTime = seekTime;
}

function updateVolume() {
    audioPlayer.volume = volumeControl.value;
}

function toggleMuteUnmute() {
    audioPlayer.muted = !audioPlayer.muted;
    muteUnmuteBtn.textContent = audioPlayer.muted ? '🔇' : '🔈';
}

function handleFiles(event) {
    const files = event.target.files;
    for (const file of files) {
        tracks.push({
            type: 'file',
            title: file.name,
            file: file
        });
    }
}

function addYouTubeTrack() {
    const url = youtubeUrlInput.value;
    const videoId = extractYouTubeID(url);
    if (videoId) {
        tracks.push({
            type: 'youtube',
            title: `YouTube Video: ${videoId}`,
            id: videoId
        });
        youtubeUrlInput.value = '';
    } else {
        alert('URL de YouTube no válida');
    }
}

function extractYouTubeID(url) {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length == 11) ? match[2] : null;
}

function loadYouTubeVideo(videoId) {
    // Aquí deberías integrar la API de YouTube para reproducir el video
    // Puedes usar el iframe API de YouTube para insertar y controlar el video
}

// Event Listeners
playPauseBtn.addEventListener('click', togglePlayPause);
nextBtn.addEventListener('click', playNextTrack);
prevBtn.addEventListener('click', playPrevTrack);
audioPlayer.addEventListener('timeupdate', updateProgressBar);
progressBar.addEventListener('input', seekTrack);
volumeControl.addEventListener('input', updateVolume);
muteUnmuteBtn.addEventListener('click', toggleMuteUnmute);
fileInput.addEventListener('change', handleFiles);
addYoutubeBtn.addEventListener('click', addYouTubeTrack);

// Load the first track
if (tracks.length > 0) {
    loadTrack(currentTrackIndex);
}
