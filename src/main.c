#include <pebble.h>
#include <stdlib.h>

Window* window;

TextLayer *strength_layer;

Layer *bars_layer;
Layer *main_layer;
Layer *window_layer;

int16_t x;
int16_t y;
int16_t z;

static void send(int key, int value) {
  DictionaryIterator *iter;
  app_message_outbox_begin(&iter);

  dict_write_int(iter, key, &value, sizeof(int), true);

  app_message_outbox_send();
}

static void outbox_sent_handler(DictionaryIterator *iter, void *context) {
  // Ready for next command
//  main_layer_set_text(main_layer, "Press up or down.");
  APP_LOG(APP_LOG_LEVEL_WARNING, "Success..!! Wohooo!!!");
}

static void outbox_failed_handler(DictionaryIterator *iter, AppMessageResult reason, void *context) {
// main_layer_set_text(main_layer, "Send failed!");
  APP_LOG(APP_LOG_LEVEL_ERROR, "Fail reason: %d", (int)reason);
}



void bars_update_callback(Layer *me, GContext* ctx) {
  (void)me;

  graphics_context_set_stroke_color(ctx, GColorWhite);

  //Strength
  graphics_draw_line(ctx, GPoint(5, 86),      GPoint(144-7, 86));
  graphics_draw_line(ctx, GPoint(5, 86+6),    GPoint(144-7, 86+6));
  graphics_draw_line(ctx, GPoint(4, 87),      GPoint(4, 87+4));
  graphics_draw_line(ctx, GPoint(144-6, 87),  GPoint(144-6, 87+4));

}


void progress_update_callback(Layer *me, GContext* ctx) {
  (void)me;
  
  graphics_context_set_stroke_color(ctx, GColorWhite);
  //Progress bars

  //strength
  int strength_percent;
  
  //% of progress bar
 // APP_LOG(APP_LOG_LEVEL_INFO, "#pu x: %d, y: %d, z: %d", x, y, z);
 
  strength_percent = x;

  graphics_context_set_fill_color(ctx, GColorWhite);
  graphics_fill_rect(ctx, GRect(5, 87, strength_percent, 5), 0, GCornerNone);

}

static void accel_data_handler(AccelData *data, uint32_t num_samples) {
  // Read sample 0's x, y, and z values
  x = abs(data[0].x)/5;
  y = abs(data[0].y)/5;
  z = abs(data[0].z)/5;
  
  
  // Determine if the sample occured during vibration, and when it occured
  bool did_vibrate = data[0].did_vibrate;
  uint64_t timestamp = data[0].timestamp;

  if(!did_vibrate) {
    
    send(0, x);
    // Print it out
    APP_LOG(APP_LOG_LEVEL_INFO, "t: %llu, x: %d, y: %d, z: %d",
                                                          timestamp, x, y, z);
    
  } else {
    // Discard with a warning
    APP_LOG(APP_LOG_LEVEL_WARNING, "Vibration occured during collection");
  }
  
  layer_set_update_proc(main_layer, progress_update_callback);
  layer_mark_dirty(main_layer);
 // text_layer_set_text(s_time_layer, x);
  
}



static void handle_init(void) {
  
  
// Open AppMessage
app_message_register_outbox_sent(outbox_sent_handler);
app_message_register_outbox_failed(outbox_failed_handler);

const int inbox_size = 128;
const int outbox_size = 128;
app_message_open(inbox_size, outbox_size);
  
  accel_data_service_subscribe(6, accel_data_handler);
  window = window_create();
  window_stack_push(window, true /* Animated */);
  window_set_background_color(window, GColorBlack);

	  window_layer = window_get_root_layer(window);
  //Texts
    //strength
    strength_layer = text_layer_create(GRect(8, 69, 144-16, 69+12));
    text_layer_set_text_color(strength_layer, GColorWhite);
    text_layer_set_background_color(strength_layer, GColorClear);
    text_layer_set_font(strength_layer, fonts_get_system_font(FONT_KEY_GOTHIC_14));
    text_layer_set_text(strength_layer, "Strength");
    layer_add_child(window_layer, text_layer_get_layer(strength_layer));

  bars_layer = layer_create(layer_get_frame(window_layer));
  layer_set_update_proc(bars_layer, bars_update_callback);
  layer_add_child(window_layer, bars_layer);

  main_layer = layer_create(layer_get_frame(window_layer));
//  layer_set_update_proc(main_layer, progress_update_callback);
  layer_add_child(window_layer, main_layer);
	
}


static void handle_destroy(void) {
  //Texts
  
  accel_data_service_unsubscribe();
    //strength
    text_layer_destroy(strength_layer);
  
  layer_destroy(bars_layer);
  
  layer_destroy(main_layer);
  
  layer_destroy(window_layer);
	
  window_destroy(window);
}


int main(void) {
   handle_init();

   app_event_loop();
	
   handle_destroy();
}



/*
static void window_load(Window *window) {
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_bounds(window_layer);


#if defined(PBL_COLOR)


#endif

static void window_unload(Window *window) {
//  menu_layer_destroy(s_menu_layer);
}

static void init() {
  s_main_window = window_create();
  window_set_window_handlers(s_main_window, (WindowHandlers) {
      .load = window_load,
      .unload = window_unload,
  });
  window_stack_push(s_main_window, true);
}

static void deinit() {
  window_destroy(s_main_window);
}

int main() {
  init();
  app_event_loop();
  deinit();
}





static void accel_data_handler(AccelData *data, uint32_t num_samples) {
  // Read sample 0's x, y, and z values
  int16_t x = data[0].x;
  int16_t y = data[0].y;
  int16_t z = data[0].z;

  // Determine if the sample occured during vibration, and when it occured
  bool did_vibrate = data[0].did_vibrate;
  uint64_t timestamp = data[0].timestamp;

  if(!did_vibrate) {
    // Print it out
    APP_LOG(APP_LOG_LEVEL_INFO, "t: %llu, x: %d, y: %d, z: %d",
                                                          timestamp, x, y, z);
  } else {
    // Discard with a warning
    APP_LOG(APP_LOG_LEVEL_WARNING, "Vibration occured during collection");
  }
  
 // text_layer_set_text(s_time_layer, x);
  
}

static void main_window_load(Window *window) {
  // Get information about the Window
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_bounds(window_layer);
  
  s_time_layer = text_layer_create(
      GRect(0, PBL_IF_ROUND_ELSE(58, 52), bounds.size.w, 50));
  
  accel_data_service_subscribe(10, accel_data_handler);

  // Create the TextLayer with specific bounds
  //s_time_layer = text_layer_create(
    //  GRect(0, PBL_IF_ROUND_ELSE(58, 52), bounds.size.w, 50));
  
//  text_layer_set_text(s_time_layer, "%d %d %d", x, y, z);
  
  

  
  // Improve the layout to be more like a watchface
  text_layer_set_background_color(s_time_layer, GColorClear);
  text_layer_set_text_color(s_time_layer, GColorBlack);
  text_layer_set_text(s_time_layer, "00:00");
  text_layer_set_font(s_time_layer, fonts_get_system_font(FONT_KEY_BITHAM_42_BOLD));
  text_layer_set_text_alignment(s_time_layer, GTextAlignmentCenter);
  
  // Add it as a child layer to the Window's root layer
  layer_add_child(window_layer, text_layer_get_layer(s_time_layer));
}
static void main_window_unload(Window *window) {
  // Destroy TextLayer
  text_layer_destroy(s_time_layer);
}
static void init() {
  // Create main Window element and assign to pointer
  s_main_window = window_create();

  // Set handlers to manage the elements inside the Window
  window_set_window_handlers(s_main_window, (WindowHandlers) {
    .load = main_window_load,
    .unload = main_window_unload
  });

  // Show the Window on the watch, with animated=true
  window_stack_push(s_main_window, true);
  
  
}

static void deinit() {
  
  accel_data_service_unsubscribe();
  // Destroy Window
  window_destroy(s_main_window);
}

int main(void) {
  init();
  app_event_loop();
  deinit();
}
*/