// Initialize the canvas and context
const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

// Game settings
const playerSpeed = 5;
const puckSpeed = 10;
const friction = 0.98;  // To make the puck slide

// Initialize objects
let player = {
  x: canvas.width / 2,
  y: canvas.height - 100,
  width: 50,
  height: 50,
  color: 'red',
  dx: 0,
  dy: 0
};

let puck = {
  x: canvas.width / 2,
  y: canvas.height / 2,
  radius: 15,
  color: 'black',
  dx: 0,
  dy: 0
};

// Controls state
const keys = {
  w: false,
  a: false,
  s: false,
  d: false,
  ArrowUp: false,
  ArrowDown: false,
  ArrowLeft: false,
  ArrowRight: false
};

// Event listeners for keypresses
window.addEventListener('keydown', (e) => {
  keys[e.key] = true;
});

window.addEventListener('keyup', (e) => {
  keys[e.key] = false;
});

// Game Loop
function gameLoop() {
  clearCanvas();
  movePlayer();
  movePuck();
  drawPlayer();
  drawPuck();
  detectCollisions();
  requestAnimationFrame(gameLoop);
}

// Clear the canvas
function clearCanvas() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// Move the player based on input
function movePlayer() {
  if (keys.w) player.dy = -playerSpeed;
  if (keys.s) player.dy = playerSpeed;
  if (keys.a) player.dx = -playerSpeed;
  if (keys.d) player.dx = playerSpeed;

  if (!keys.w && !keys.s) player.dy = 0;
  if (!keys.a && !keys.d) player.dx = 0;

  // Update player position
  player.x += player.dx;
  player.y += player.dy;

  // Boundary checks
  if (player.x < 0) player.x = 0;
  if (player.x + player.width > canvas.width) player.x = canvas.width - player.width;
  if (player.y < 0) player.y = 0;
  if (player.y + player.height > canvas.height) player.y = canvas.height - player.height;
}

// Move the puck
function movePuck() {
  // Shooting puck with arrow keys
  if (keys.ArrowUp) puck.dy = -puckSpeed;
  if (keys.ArrowDown) puck.dy = puckSpeed;
  if (keys.ArrowLeft) puck.dx = -puckSpeed;
  if (keys.ArrowRight) puck.dx = puckSpeed;

  // Update puck position
  puck.x += puck.dx;
  puck.y += puck.dy;

  // Apply friction to slow down the puck
  puck.dx *= friction;
  puck.dy *= friction;

  // Boundary checks for puck
  if (puck.x - puck.radius < 0 || puck.x + puck.radius > canvas.width) puck.dx *= -1;
  if (puck.y - puck.radius < 0 || puck.y + puck.radius > canvas.height) puck.dy *= -1;
}

// Draw the player (as Ottawa Senators player)
function drawPlayer() {
  ctx.fillStyle = player.color;
  ctx.fillRect(player.x, player.y, player.width, player.height);
}

// Draw the puck
function drawPuck() {
  ctx.beginPath();
  ctx.arc(puck.x, puck.y, puck.radius, 0, Math.PI * 2);
  ctx.fillStyle = puck.color;
  ctx.fill();
  ctx.closePath();
}

// Basic collision detection between player and puck
function detectCollisions() {
  const distX = Math.abs(puck.x - (player.x + player.width / 2));
  const distY = Math.abs(puck.y - (player.y + player.height / 2));

  if (distX < player.width / 2 + puck.radius && distY < player.height / 2 + puck.radius) {
    // Reflect puck off the player
    puck.dx = (puck.x - (player.x + player.width / 2)) * 0.2;
    puck.dy = (puck.y - (player.y + player.height / 2)) * 0.2;
  }
}

// Start the game loop
gameLoop();
