package com.yakrm.codeclinic.yakrm.Adapter;

import org.zakariya.stickyheaders.SectioningAdapter;

public class NotificationAdapter extends SectioningAdapter {
  /*  Locale locale = Locale.getDefault();
    static final boolean USE_DEBUG_APPEARANCE = false;

    ArrayList<String> arrayList;
    ArrayList<String> title_arrayList;

    public NotificationAdapter(ArrayList<String> arrayList, ArrayList<String> title_arrayList) {
        super();
        this.arrayList = arrayList;
        this.title_arrayList = title_arrayList;
    }

    private class Section {
        String alpha;
        ArrayList<Person> people = new ArrayList<>();
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
        TextView tv_notification;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_notification = (TextView) itemView.findViewById(R.id.tv_notification);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }


    List<Person> people;
    ArrayList<Section> sections = new ArrayList<>();

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
        sections.clear();

        // sort people into buckets by the first letter of last name
        char alpha = 0;
        Section currentSection = null;
        for (Person person : people) {
            if (person.name.last.charAt(0) != alpha) {
                if (currentSection != null) {
                    sections.add(currentSection);
                }

                currentSection = new Section();
                alpha = person.name.last.charAt(0);
                currentSection.alpha = String.valueOf(alpha);
            }

            if (currentSection != null) {
                currentSection.people.add(person);
            }
        }

        sections.add(currentSection);
        notifyAllSectionsDataSetChanged();
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).people.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_notification_list_items, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_notification_header_view, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        Person person = s.people.get(itemIndex);
        ivh.tv_notification.setText(capitalize(person.name.last) + ", " + capitalize(person.name.first));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;

        if (USE_DEBUG_APPEARANCE) {
            hvh.itemView.setBackgroundColor(0x55ffffff);
            hvh.titleTextView.setText(pad(sectionIndex * 2) + s.alpha);
        } else {
            hvh.titleTextView.setText(s.alpha);
        }
    }

    private String capitalize(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase(locale) + s.substring(1);
        }

        return "";
    }

    private String pad(int spaces) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            b.append(' ');
        }
        return b.toString();
    }*/
}
