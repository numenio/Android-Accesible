package info.thewordnerd.touchtype

import collection.JavaConversions._

import android.content.Context
import android.content.res.{Resources, XmlResourceParser}
import android.inputmethodservice.{Keyboard, KeyboardView}
import android.inputmethodservice.Keyboard.{Key, Row}
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

import com.nullwire.trace.ExceptionHandler

class LatinKeyboard(c:Context, layoutID:Int) extends Keyboard(c, layoutID) {

	private var enterKey:Option[Key] = getKeys.filter(_.codes(0) == 10).headOption

			def setImeOptions(res:Resources, options:Int) {
	enterKey.foreach { enter =>
	options&(EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION) match {
	case EditorInfo.IME_ACTION_GO =>
	enter.iconPreview = null
	enter.icon = null
	enter.label = res.getText(R.string.go_label)
	case EditorInfo.IME_ACTION_NEXT =>
	enter.iconPreview = null
	enter.icon = null
	enter.label = res.getText(R.string.next_label)
	case EditorInfo.IME_ACTION_SEARCH =>
	enter.icon = res.getDrawable(R.drawable.sym_keyboard_search)
	enter.label = res.getText(R.string.search_label)
	case EditorInfo.IME_ACTION_SEND =>
	enter.iconPreview = null
	enter.icon = null
	enter.label = res.getText(R.string.send_label)
	case _ =>
	enter.icon = res.getDrawable(R.drawable.sym_keyboard_return)
	enter.label = null
	}
	}
}

}

import collection.JavaConversions._

import android.view.MotionEvent
import android.util.{AttributeSet, Log}

class LatinKeyboardView(c:Context, attrs:AttributeSet) extends KeyboardView(c, attrs) {

	val keycodeOptions = -100

			private[touchtype] var service:Touchtype = null

			private var lastNearestKey:Key = null

			private var startX = 0f
			private var startY = 0f
			private var startTime = 0l

			override def onTouchEvent(event:MotionEvent):Boolean = {
					if(event.getAction == MotionEvent.ACTION_UP) {
						lastNearestKey = null
								if(service.gestureMagnitude > 0) {
									val direction = math.atan2(startY-event.getY, startX-event.getX)
											Log.d("keyboardcheck", "Direction: "+direction)
											if(direction < 0.25*math.Pi && direction > -0.25*math.Pi)
												swipeLeft()
												else if(direction < -0.25*math.Pi && direction > -0.75*math.Pi)
													swipeDown()
													else if(direction < -0.75*math.Pi || direction > 0.75*math.Pi)
														swipeRight()
														else
															swipeUp()
								}
						startX = 0
								startY = 0
								startTime = 0
					} else {
						if(startX == 0) {
							startX = event.getX
									startY = event.getY
									startTime = System.currentTimeMillis
						}
						val magnitude = event.getPointerCount-1
								if(service.gestureMagnitude < magnitude)
									service.gestureMagnitude = magnitude
									if(service.gestureMagnitude != 0 && lastNearestKey != Keyboard.KEYCODE_DELETE) {
										lastNearestKey = null
												service.lastProcessedKey = None
									} else {
										if(System.currentTimeMillis-startTime <= 50) return super.onTouchEvent(event)
												val x = event.getX.toInt
												val y = event.getY.toInt
												val nearestKey:Option[Key] = getKeyboard.getNearestKeys(x, y)
												.map(getKeyboard.getKeys.get(_))
												.sortWith { (v1, v2) =>
												v1.squaredDistanceFrom(x, y) <= v2.squaredDistanceFrom(x, y)
										}.headOption
										nearestKey.foreach { nearest =>
										if(nearest != lastNearestKey) {
											service.processKey(nearest, Normal)
										}
										lastNearestKey = nearest
										}
									}
					}
					super.onTouchEvent(event)
			}

}

sealed trait KeyType
object Normal extends KeyType
object Complete extends KeyType


import android.inputmethodservice.InputMethodService
import android.text.method.MetaKeyKeyListener
import android.view.{KeyCharacterMap, View}
import android.view.inputmethod.{CompletionInfo, EditorInfo, InputConnection, InputMethodManager}
import android.text.InputType
import android.speech.tts.TextToSpeech
import TextToSpeech._
import android.view.inputmethod.{ExtractedText, ExtractedTextRequest}

import java.util.{ArrayList, List => JList}

class Touchtype extends InputMethodService with OnInitListener with KeyboardView.OnKeyboardActionListener {

	private var inputView:Option[KeyboardView] = None

			private var lastDisplayWidth = 0

			private var wordSeparators = ""

			private var tts:TextToSpeech = null

			override def onCreate() {
	super.onCreate()
	Preferences(this)
	if(Preferences.sendCrashReports_?)
		ExceptionHandler.register(this, "http://stacktrace.thewordnerd.info/")
		tts = new TextToSpeech(this, this)
}

def onInit(status:Int) {
	// TODO: Should probably check for presence of required language
	tts.setLanguage(java.util.Locale.getDefault)
}

override def onDestroy() {
	super.onDestroy()
	tts.shutdown()
}

private val managedPunctuations = Map(
		"" -> R.string.blank,
		"!" -> R.string.exclamation,
		"@" -> R.string.at,
		"#" -> R.string.pound,
		"$" -> R.string.dollar,
		"%" -> R.string.percent,
		"^" -> R.string.caret,
		"&" -> R.string.ampersand,
		"*" -> R.string.asterisk,
		"(" -> R.string.leftParen,
		")" -> R.string.rightParen,
		"_" -> R.string.underscore,
		" " -> R.string.space,
		"." -> R.string.period,
		"," -> R.string.comma,
		"<" -> R.string.lessThan,
		">" -> R.string.greaterThan,
		"/" -> R.string.slash,
		"\\" -> R.string.backslash,
		"?" -> R.string.questionMark,
		"\"" -> R.string.quote,
		"'" -> R.string.apostrophe,
		";" -> R.string.semiColon,
		":" -> R.string.colon,
		"-" -> R.string.dash,
		"+" -> R.string.plus,
		"\n" -> R.string.newline
		)

		private def say(str:String, pitch:Float = 1) {
	tts.setPitch(pitch)
	managedPunctuations.get(str).map { str =>
	tts.speak(getString(str), QUEUE_FLUSH, null)
	}.getOrElse {
		if(str.length == 1 && str >= "A" && str <= "Z")
			tts.speak(getString(R.string.cap, str), QUEUE_FLUSH, null)
			else
				tts.speak(str, QUEUE_FLUSH, null)
	}
	tts.setPitch(1)
}

private var qwertyKeyboard:Option[LatinKeyboard] = None
private var symbolsKeyboard:Option[LatinKeyboard] = None
private var symbolsShiftedKeyboard:Option[LatinKeyboard] = None
private var curKeyboard:Option[LatinKeyboard] = None

override def onInitializeInterface() {
		Log.d("touchtype", "onInitializeInterface()")
		qwertyKeyboard.getOrElse {
			val displayWidth = getMaxWidth
					if(displayWidth == lastDisplayWidth) return
							lastDisplayWidth = displayWidth
		}
		qwertyKeyboard = Some(new LatinKeyboard(this, R.xml.qwerty))
				symbolsKeyboard = Some(new LatinKeyboard(this, R.xml.symbols))
				symbolsShiftedKeyboard = Some(new LatinKeyboard(this, R.xml.symbols_shift))
	}

	override def onCreateInputView() = {
		Log.d("touchtype", "onCreateInputView()")
		val view = getLayoutInflater().inflate(R.layout.input, null).asInstanceOf[LatinKeyboardView]
				view.service = this
				view.setOnKeyboardActionListener(this)
				view.setKeyboard(curKeyboard.getOrElse(qwertyKeyboard.get))
				inputView = Some(view)
				view
	}

	private[touchtype] var gestureMagnitude = 0

			private[touchtype] var lastProcessedKey:Option[Key] = None

			def processKey(key:Key, keyType:KeyType = Normal) {
				val pitch = keyType match {
				case Normal => 1f
				case Complete => 1.5f
				case _ => 0.8f
				}
				if(key.codes(0) == Keyboard.KEYCODE_SHIFT) {
					val keyboard = inputView.get.getKeyboard
							if(curKeyboard == qwertyKeyboard) {
								if(keyboard.isShifted) {
									if(shiftLocked)
										say(getString(R.string.shift_locked))
										else
											say(getString(R.string.shift_on))
								} else
									say(getString(R.string.shift_off))
							} else if(curKeyboard == symbolsKeyboard)
								say(getString(R.string.symbols_label))
								else
									say(getString(R.string.more_symbols_label))
				} else if(key.codes(0) == Keyboard.KEYCODE_MODE_CHANGE) {
					if(curKeyboard == qwertyKeyboard) say(getString(R.string.mode_letters))
					else if(curKeyboard == symbolsKeyboard || curKeyboard == symbolsShiftedKeyboard)
						say(getString(R.string.mode_symbols))
				} else if(Character.isLetterOrDigit(key.codes(0).toChar)) {
					var char = key.codes(0).toChar.toString
							val shifted = inputView.map(_.isShifted).getOrElse(false)
							if(shifted) char = char.toUpperCase
							say(char)
				} else if(key.label != null)
					say(key.label.toString, pitch)
					else {
						say("Unlabeled key", pitch)
						Log.d("keyboardcheck", "Unlabeled key: "+key)
					}
				lastProcessedKey = Some(key)
			}

			private def updateShiftKeyState(attr:EditorInfo) {
				inputView.foreach { view =>
				if(attr != null && curKeyboard == qwertyKeyboard) {
					val info = getCurrentInputEditorInfo
							var caps = 0
							if(info != null && info.inputType != 0)
								caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType)
								view.setShifted(shiftLocked || caps != 0)
				}
				}
			}

			private var wasSoftKeyboardShown = false

					private var inputting_? = false

					private def content:Option[String] = Option(getCurrentInputConnection).map { ic =>
					val extract = ic.getExtractedText(new ExtractedTextRequest(), 0)
					extract.text.toString
			}

			override def onStartInput(attribute:EditorInfo, restarting:Boolean) {
				Log.d("touchtype", "onStartInput("+attribute.inputType+", "+restarting+")")
				if(attribute.inputType != 0) inputting_? = true
				super.onStartInput(attribute, restarting)
				updateShiftKeyState(attribute)
				(attribute.inputType&InputType.TYPE_MASK_CLASS) match {
				case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_DATETIME =>
				curKeyboard = symbolsKeyboard
				case InputType.TYPE_CLASS_PHONE =>
				curKeyboard = symbolsKeyboard
				case _ =>
				curKeyboard = qwertyKeyboard
				}
				curKeyboard.foreach(_.setImeOptions(getResources(), attribute.imeOptions))
				altOn = false
				altLocked = false
				inputView.getOrElse(onCreateInputView())
				if(wasSoftKeyboardShown) {
					wasSoftKeyboardShown = false
							say(getString(R.string.soft_keyboard_off))
				}
			}

			override def onFinishInput() {
				Log.d("touchtype", "onFinishInput()")
				super.onFinishInput()
				inputting_? = false
				curKeyboard = qwertyKeyboard
				inputView.foreach { v =>
				v.setKeyboard(curKeyboard.get)
				v.closing()
				}
			}

			override def onStartInputView(attribute:EditorInfo, restarting:Boolean) {
				Log.d("touchtype", "onStartInputView("+attribute+", "+restarting+")")
				super.onStartInputView(attribute, restarting)
				inputView.foreach { view =>
				curKeyboard.foreach(view.setKeyboard(_))
				view.closing()
				}
				if(isInputViewShown && !wasSoftKeyboardShown) {
					wasSoftKeyboardShown = true
							say(getString(R.string.soft_keyboard_on))
				}
			}

			private var cursorPosition = 0

					private var shouldSpeakNextSelection = false

					override def onUpdateSelection(oldSelStart:Int, oldSelEnd:Int, newSelStart:Int, newSelEnd:Int, candidatesStart:Int, candidatesEnd:Int) {
				super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd)
				cursorPosition = newSelStart
				if(shouldSpeakNextSelection) {
					content.foreach { c =>
					Log.d("keyboardcheck", "Change: "+oldSelStart+", "+oldSelEnd+". "+newSelStart+", "+newSelEnd)
					var start = 0
					var end = 0
					if(oldSelStart < newSelStart) {
						start = newSelStart
								end = newSelEnd+1
					} else {
						start = newSelEnd
								end = oldSelStart
					}
					val text = if(end > c.length)
						getString(R.string.end)
						else c.substring(start, end)
						Log.d("keyboardcheck", "Should speak from "+start+" to "+end+" on "+c+": "+text)
						say(text)
					}
				}
				shouldSpeakNextSelection = false
			}

			override def onKeyDown(keyCode:Int, event:KeyEvent):Boolean = {
					keyCode match {
					case KeyEvent.KEYCODE_SEARCH if(Preferences.speakActionKeys_?) =>
					say(getString(R.string.search_label))
					case KeyEvent.KEYCODE_BACK if(event.getRepeatCount == 0) =>
					if(Preferences.speakActionKeys_?)
						say(getString(R.string.back_label))
						inputView.foreach { view =>
						if(view.handleBack()) return true
					}
					case KeyEvent.KEYCODE_MENU if(Preferences.speakActionKeys_?) =>
					say(getString(R.string.menu_label))
					case KeyEvent.KEYCODE_HOME if(Preferences.speakActionKeys_?) =>
					say(getString(R.string.home_label))
					case KeyEvent.KEYCODE_SHIFT_LEFT | KeyEvent.KEYCODE_SHIFT_RIGHT if(event.getRepeatCount == 0) =>
					handleShift()
					case KeyEvent.KEYCODE_ALT_LEFT | KeyEvent.KEYCODE_ALT_RIGHT if(event.getRepeatCount == 0) =>
					handleAlt()
					case _ =>
					if(!altLocked)
						altOn = false
						updateShiftKeyState(getCurrentInputEditorInfo)
					}
					super.onKeyDown(keyCode, event)
			}

			override def onKeyUp(keyCode:Int, event:KeyEvent):Boolean = {
					def clearStickyStates() {
						altOn = false
								altLocked = false
					}
					keyCode match {
					case KeyEvent.KEYCODE_DPAD_LEFT if(inputting_? && event.getRepeatCount == 0) =>
					clearStickyStates()
					shouldSpeakNextSelection = true
					case KeyEvent.KEYCODE_DPAD_RIGHT if(inputting_? && event.getRepeatCount == 0) =>
					clearStickyStates()
					shouldSpeakNextSelection = true
					case KeyEvent.KEYCODE_DPAD_UP if(inputting_? && event.getRepeatCount == 0) =>
					val rv = super.onKeyUp(keyCode, event)
					lineUp()
					clearStickyStates()
					return rv
					case KeyEvent.KEYCODE_DPAD_DOWN if(inputting_? && event.getRepeatCount == 0) =>
					val rv = super.onKeyUp(keyCode, event)
					lineDown()
					clearStickyStates()
					return rv
					case _ =>
					}
					super.onKeyUp(keyCode, event)
			}

			def onKey(primaryCode:Int, keyCodes:Array[Int]) {
				primaryCode match {
				case Keyboard.KEYCODE_DELETE => handleDelete()
				case Keyboard.KEYCODE_SHIFT => handleShift()
				case Keyboard.KEYCODE_CANCEL => handleClose()
				case Keyboard.KEYCODE_MODE_CHANGE => handleModeChange()
				case _ => handleCharacter(primaryCode)
				}
				gestureMagnitude = 0
						if(primaryCode != Keyboard.KEYCODE_SHIFT && !shiftLocked)
							inputView.foreach(_.setShifted(false))
			}

			def onText(text:CharSequence) {
			}

			private def handleDelete() {
				say(getCurrentInputConnection.getTextBeforeCursor(1, 0).toString, 0.8f)
				getCurrentInputConnection.deleteSurroundingText(1, 0)
			}

			private var shiftLocked = false

					private def handleShift() {
				inputView.map { view =>
				val keyboard = view.getKeyboard
				val shift = keyboard.isShifted
				if(curKeyboard == qwertyKeyboard) {
					if(shift) {
						if(shiftLocked) {
							keyboard.setShifted(false)
							shiftLocked = false
							if(isInputViewShown || Preferences.speakModifierKeys_?)
								say(getString(R.string.shift_off))
						} else {
							shiftLocked = true
									if(isInputViewShown || Preferences.speakModifierKeys_?)
										say(getString(R.string.shift_locked))
						}
					} else {
						keyboard.setShifted(true)
						if(isInputViewShown || Preferences.speakModifierKeys_?)
							say(getString(R.string.shift_on))
					}
				} else if(curKeyboard == symbolsKeyboard) {
					curKeyboard = symbolsShiftedKeyboard
							inputView.foreach(_.setKeyboard(curKeyboard.get))
							say(getString(R.string.more_symbols_label))
				} else {
					curKeyboard = symbolsKeyboard
							inputView.foreach(_.setKeyboard(curKeyboard.get))
							say(getString(R.string.symbols_label))
				}
				}.getOrElse(say(getString(R.string.shift_on)))
			}

			private var altOn = false
					private var altLocked = false

					private def handleAlt() {
				if(altOn) {
					if(altLocked) {
						altOn = false
								altLocked = false
								if(Preferences.speakModifierKeys_?)
									say(getString(R.string.alt_off))
					} else {
						altLocked = true
								if(Preferences.speakModifierKeys_?)
									say(getString(R.string.alt_locked))
					}
				} else {
					altOn = true
							altLocked = false
							if(Preferences.speakModifierKeys_?)
								say(getString(R.string.alt_on))
				}
			}

			private def handleCharacter(keyCode:Int) {
				lastProcessedKey.foreach { key =>
				inputView.foreach { view =>
				val str = if(view.isShifted)
					Character.toUpperCase(key.codes(0)).toChar.toString
					else key.codes(0).toChar.toString
					getCurrentInputConnection.commitText(str, 1)
					processKey(key, Complete)
				}
				}
				updateShiftKeyState(getCurrentInputEditorInfo)
			}

			private def handleClose() {
			}

			private def handleModeChange() {
				shiftLocked = false
						if(curKeyboard == qwertyKeyboard)
							curKeyboard = symbolsKeyboard
							else
								curKeyboard = qwertyKeyboard
								inputView.foreach { view =>
								view.setKeyboard(curKeyboard.get)
								view.setShifted(false)
							}
				lastProcessedKey.foreach(processKey(_))
			}

			def swipeRight() {
				gestureMagnitude match {
				case 1 =>
				shouldSpeakNextSelection = true
				sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT)
				case _ =>
				}
			}

			def swipeLeft() {
				gestureMagnitude match {
				case 1 =>
				shouldSpeakNextSelection = true
				sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT)
				case _ =>
				}
			}

			def swipeDown() {
				say("Swipe down.")
			}

			def swipeUp() {
				say("Swipe up.")
			}

			def onPress(primaryCode:Int) {
			}

			def onRelease(primaryCode:Int) {
			}

			def lineUp() {
				val before = getCurrentInputConnection.getTextBeforeCursor(65535, 0).toString
						val after = getCurrentInputConnection.getTextAfterCursor(65535, 0).toString
						if(!(before+after).contains("\n")) return
								var line = before.substring(before.lastIndexOf("\n")+1, before.length)
								if(before.indexOf("\n") == -1)
									line = "Top. "+line
									val test = after.indexOf("\n")
									if(test > 0)
										line += after.substring(0, test)
										say(line)
			}

			def lineDown() {
				val beforeVal = getCurrentInputConnection.getTextBeforeCursor(65535, 0)
						val before = if(beforeVal == null) "" else beforeVal.toString
						val afterVal = getCurrentInputConnection.getTextAfterCursor(65535, 0)
						val after = if(afterVal == null) "" else afterVal.toString
						if(!(before+after).contains("\n")) return
								var line = before.substring(before.lastIndexOf("\n")+1, before.length)
								if(after.indexOf("\n") == -1)
									line = "Bottom. "+line
									val test = after.indexOf("\n")
									if(test >= 0)
										line += after.substring(0, test)
										else
											line += after.substring(0, after.length)
											say(line)
			}

}

import android.content.{Context, SharedPreferences}
import android.preference.PreferenceManager

object Preferences {

	private var preferences:SharedPreferences = null

			def apply(c:Context) {
	preferences = PreferenceManager.getDefaultSharedPreferences(c)
}

def speakActionKeys_? =     preferences.getBoolean("speak_action_keys", true)

def speakModifierKeys_? =     preferences.getBoolean("speak_modifier_keys", true)

def sendCrashReports_? =     preferences.getBoolean("send_crash_reports", true)

}

import android.os.Bundle
import android.preference.PreferenceActivity

class PreferencesActivity extends PreferenceActivity {
	override def onCreate(bundle:Bundle) {
		super.onCreate(bundle)
		addPreferencesFromResource(R.xml.preferences)
	}
}