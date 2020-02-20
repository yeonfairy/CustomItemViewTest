//package com.example.kwlee.customitemviewtest.models;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import it.feio.android.omninotes.commons.models.BaseAttachment;
//import it.feio.android.omninotes.commons.models.BaseCategory;
//import it.feio.android.omninotes.commons.models.BaseNote;
//
//
//public class Note extends BaseNote implements Parcelable {
//
//  /*
//   * Parcelable interface must also have a static field called CREATOR, which is an object implementing the
//   * Parcelable.Creator interface. Used to un-marshal or de-serialize object from Parcel.
//   */
//  public static final Creator<Note> CREATOR = new Creator<Note>() {
//
//    public Note createFromParcel (Parcel in) {
//      return new Note(in);
//    }
//
//
//    public Note[] newArray (int size) {
//      return new Note[size];
//    }
//  };
//  // Not saved in DB
//  private boolean passwordChecked = false;
//
//
//  public Note() {
//    super();
//  }
//
//
//  public Note(Long creation, Long lastModification, String title, String content, Integer archived,
//              Integer trashed, String alarm, String recurrenceRule, Integer reminderFired, String latitude, String longitude,
//              Category
//          category, Integer locked, Integer checklist) {
//    super(creation, lastModification, title, content, archived, trashed, alarm, reminderFired, recurrenceRule,
//        latitude,
//        longitude, category, locked, checklist);
//  }
//
//
//  public Note(Note note) {
//    super(note);
//    setPasswordChecked(note.isPasswordChecked());
//  }
//
//
//  private Note(Parcel in) {
//    setCreation(in.readString());
//    setLastModification(in.readString());
//    setTitle(in.readString());
//    setContent(in.readString());
//    setTrashed(in.readInt());
//    super.setCategory(in.readParcelable(Category.class.getClassLoader()));
//    in.readList(getAttachmentsList(), Attachment.class.getClassLoader());
//  }
//
//  public List<Attachment> getAttachmentsList () {
////		List<Attachment> list = new ArrayList<>();
////		for (it.feio.android.omninotes.commons.models.Attachment attachment : super.getAttachmentsList()) {
////			if (attachment.getClass().equals(Attachment.class)) {
////				list.add((Attachment) attachment);
////			} else {
////				list.add(new Attachment(attachment));
////			}
////		}
////		return list;
//    // FIXME This fixes https://github.com/federicoiosue/Omni-Notes/issues/199 but could introduce other issues
//    return (List<Attachment>) super.getAttachmentsList();
//  }
//
//  public void setAttachmentsList (ArrayList<Attachment> attachmentsList) {
//    super.setAttachmentsList(attachmentsList);
//  }
//
//  public void addAttachment (Attachment attachment) {
//    List<Attachment> attachmentsList = ((List<Attachment>) super.getAttachmentsList());
//    attachmentsList.add(attachment);
//    setAttachmentsList(attachmentsList);
//  }
//
//  public void removeAttachment (Attachment attachment) {
//    List<Attachment> attachmentsList = ((List<Attachment>) super.getAttachmentsList());
//    attachmentsList.remove(attachment);
//    setAttachmentsList(attachmentsList);
//  }
//
//  public List<Attachment> getAttachmentsListOld () {
//    return (List<Attachment>) super.getAttachmentsListOld();
//  }
//
//  public void setAttachmentsListOld (ArrayList<Attachment> attachmentsListOld) {
//    super.setAttachmentsListOld(attachmentsListOld);
//  }
//
//  public boolean isPasswordChecked () {
//    return passwordChecked;
//  }
//
//  public void setPasswordChecked (boolean passwordChecked) {
//    this.passwordChecked = passwordChecked;
//  }
//
//
//  @Override
//  public void buildFromJson (String jsonNote) {
//    super.buildFromJson(jsonNote);
//    List<Attachment> attachments = new ArrayList<>();
//    for (BaseAttachment attachment : getAttachmentsList()) {
//      attachments.add(new Attachment(attachment));
//    }
//    setAttachmentsList(attachments);
//  }
//
//  @Override
//  public int describeContents () {
//    return 0;
//  }
//}
